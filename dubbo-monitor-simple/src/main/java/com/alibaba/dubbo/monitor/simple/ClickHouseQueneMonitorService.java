package com.alibaba.dubbo.monitor.simple;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.utils.ConfigUtils;
import com.alibaba.dubbo.monitor.MonitorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class ClickHouseQueneMonitorService implements MonitorService {

    Logger logger = LoggerFactory.getLogger(getClass());
    BlockingDeque<URL> queue = new LinkedBlockingDeque<>();
    int len = 1000;

    public ClickHouseQueneMonitorService(){
        Thread t = new Thread(()->{
            while (true){
                int i = 0;
                List<URL> list = new ArrayList<>();
                try{
                    i++;
                    URL url = queue.take();
                    list.add(url);
                    if( i >= len){
                        i = 0;
                        batchInsert(list);
                        list.clear();
                    }
                }catch (Exception e){
                    logger.error(e.getMessage(),e);
                }
            }
        });
        t.start();
    }

    @Override
    public void collect(URL statistics) {
        queue.add(statistics);
    }

    volatile private Date d = new Date();

    public void batchInsert(List<URL> list) {
        if ( list.size() == 0 ) return;

        List<StatisticsModel> statisticsModels = new ArrayList<>();
        for(URL url : list){
            StatisticsModel model = new StatisticsModel(url);
            statisticsModels.add(model);
        }
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = getConnection();
            String sql = "INSERT INTO db_logs " +
                    "(create_at,service,method,provider,consumer,failure,success,elapsed," +
                    "concurrent,input,output,maxConcurrent,maxElapsed,maxInput,maxOutput,host,port) " +
                    "VALUES(?, ?, ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            pstmt = conn.prepareStatement(sql);

            for (StatisticsModel model: statisticsModels) {
                pstmt.setTimestamp(1, new Timestamp(model.getCreateAt().getTime()));
                pstmt.setString(2, model.getService());
                pstmt.setString(3, model.getMethod());
                pstmt.setString(4, model.getProvider());
                pstmt.setString(5, model.getConsumer());
                pstmt.setInt(6, model.getFailure());
                pstmt.setInt(7, model.getSuccess());
                pstmt.setInt(8, model.getElapsed());
                pstmt.setInt(9, model.getConcurrent());
                pstmt.setInt(10, model.getInput());
                pstmt.setInt(11, model.getOutput());
                pstmt.setInt(12, model.getMaxConcurrent());
                pstmt.setInt(13, model.getMaxElapsed());
                pstmt.setInt(14, model.getMaxInput());
                pstmt.setInt(15, model.getMaxOutput());
                pstmt.setString(16, model.getHost());
                pstmt.setInt(17, model.getPort());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        } catch (Exception e) {
           logger.error(e.getMessage(),e);
        } finally {
            d = new Date();
            try{
                if (pstmt != null ){
                    pstmt.close();
                }
                if ( conn != null) {
                    conn.close();
                }
            }catch (Exception e){
                logger.error(e.getMessage(),e);
            }

        }
    }



    private Connection getConnection() throws ClassNotFoundException, SQLException {
        String host  = ConfigUtils.getProperty("clickhouse.host", "jdbc:clickhouse://10.10.20.15:9000/app");
        Class.forName("com.github.housepower.jdbc.ClickHouseDriver");
        Connection conn = DriverManager.getConnection(host);
        return conn;
    }


    @Override
    public List<URL> lookup(URL query) {
        return null;
    }
}
