package com.tams.websocket.audit;

import com.tams.websocket.command.commands.models.TimeLogRequest;
import com.tams.websocket.utils.Utilities;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class ManvishProcessRawLog {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PersistenceContext
    private EntityManager entityManager;

    @Async("taskExecutor")
    public void processManvishData(TimeLogRequest request) {
        log.info("Manvish STOREprocesser:" + request);
        LocalDateTime dateTme = Utilities.convertStringTimestamp(request.getTime(), Utilities.YYY_MM_DD_T_HH_MM_SS_X);
        String date = Utilities.formatLocalDateTimeToUTCString(dateTme, "d/M/yyyy");
        String time = Utilities.formatLocalDateTimeToUTCString(dateTme, Utilities.HH_MM_SS);
        log.info("STORE processer:" + dateTme +"/" + date + "/" + time);
        this.insertAcslogData(request);
        int pkacslog = this.getAcslogData(request);
        String processData = this.processAcslogData(request, date, time, pkacslog);
        log.info("STORE processer response:" + processData);
        this.updateAcslogData(0, "Y");
    }

    @Transactional
    public void insertAcslogData(TimeLogRequest request) {
        try {
            String query = "insert into acslog_rawdata(unitid, acsdate, deptid, empid, acstime, inoutmode, emptype, compid, " +
                    "empmode, DELETEDFLAG, processflag, longitude, latitude, modifieddate) " +
                    "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            jdbcTemplate.update(query,
                    request.getTerminalID(), // unitid
                    Utilities.convertString2Date(request.getTime(), Utilities.YYY_MM_DD_T_HH_MM_SS_X), // acsdate
                    1,  // Example department ID
                    request.getUserID(), // empid
                    Utilities.convertStringTimestamp(request.getTime(), Utilities.YYY_MM_DD_T_HH_MM_SS_X), // acstime
                    //Utilities.convertString2Time(request.getTime(), Utilities.YYY_MM_DD_T_HH_MM_SS_X), // acstime
                    "IN",  // inoutmode
                    "001",  // Example emptype
                    1,  // Example compid
                    32,  // Example empmode
                    "N",  // DELETEDFLAG
                    "N",  // processflag
                    "-",  // longitude
                    "-",  // latitude
                    Utilities.getCurrentDateTime()
            );
        } catch (Exception se) {
            se.printStackTrace();
        }
    }

    public int getAcslogData(TimeLogRequest request) {
        try {
            String query = "select pkacslog from acslog_rawdata where unitid = ? and empid = ? and processflag = 'N' order by 1 desc;";
            List<Integer> results = jdbcTemplate.query(
                    query,
                    new Object[]{request.getTerminalID(), request.getUserID()},
                    (rs, rowNum) -> rs.getInt("pkacslog")
            );
            log.info("Selected PKACSLOG :" + results);
            return (results.isEmpty()) ? 0 : results.get(0);
        } catch (Exception se) {
            se.printStackTrace();
        }
        return 0;
    }

    @Transactional
    public int updateAcslogData(int pkcode, String status) {
        try {
            String sql = "UPDATE acslog_rawdata SET processflag = '" + status + "' WHERE pkacslog = ?";
            return jdbcTemplate.update(sql, pkcode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    @Transactional
    public String processAcslogData(TimeLogRequest request, String date, String time, int pkacslog) {
        try {
            this.updateAcslogData(pkacslog, "P");

            StoredProcedureQuery query = entityManager.createStoredProcedureQuery("insert_acslog_process_data");
            // Register input parameters
            query.registerStoredProcedureParameter("p_unitcode", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_date", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_deptid", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_empid", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_time", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_inout", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_emptype", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_compid", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_empmode", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_utc_date", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_utc_time", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_time_Zone", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_longitude", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_latitude", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_rawtablepkcode", Integer.class, ParameterMode.IN);
            // Register output parameter
            query.registerStoredProcedureParameter("P_errormessage", String.class, ParameterMode.OUT);

            // Set input parameter values
            query.setParameter("p_unitcode", request.getTerminalID());
            query.setParameter("p_date", date);
            query.setParameter("p_deptid", "1");
            query.setParameter("p_empid", request.getUserID());
            query.setParameter("p_time", time);
            query.setParameter("p_inout", "IN");
            query.setParameter("p_emptype", "001");
            query.setParameter("p_compid", "1");
            query.setParameter("p_empmode", "32");
            query.setParameter("p_utc_date", "");
            query.setParameter("p_utc_time", "");
            query.setParameter("p_time_Zone", "");
            query.setParameter("p_longitude", "-");
            query.setParameter("p_latitude", "-");
            query.setParameter("p_rawtablepkcode", pkacslog);

            // Execute the stored procedure
            query.execute();
            // Get the output parameter value
            String errorMessage = (String) query.getOutputParameterValue("P_errormessage");
            return errorMessage;
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

}
