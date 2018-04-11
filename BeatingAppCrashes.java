String selectQueryApp = "SELECT id FROM conf_system_status where app_name = ? AND active = 1 AND date_modified < (NOW() - INTERVAL 20 MINUTE)";
        logger.info("Main Thread: "+selectQueryApp);
        PreparedStatement stmtqueryapp;
        ResultSet rsapp = null;
        int h = 0;
        try {
            stmtqueryapp = conn.prepareStatement(selectQueryApp);
            stmtqueryapp.setString(1, "AppName");
            logger.info("Main Thread: Select Query: "+stmtqueryapp.toString());
            rsapp = stmtqueryapp.executeQuery();                
            while (rsapp.next()) {
                app_id = rsapp.getInt("id");
                String updateQueryApp = "UPDATE conf_system_status set last_time_started = current_timestamp, start_count = start_count + ?, active = ? WHERE id = ?";
                PreparedStatement stmtupdateapp = conn.prepareStatement(updateQueryApp);
                stmtupdateapp.setInt(1,1);
                stmtupdateapp.setInt(2,1);
                stmtupdateapp.setInt(3, app_id);
                logger.info("Main Thread: Update Query: "+stmtupdateapp.toString());
                stmtupdateapp.executeUpdate();
                logger.info("Main Thread: Update Query Executed!");
                h++;                    
             }
            if(0 == h){
                try {
                    conn.close();
                    logger.info("Closing DB connection!");
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } 
                logger.info("Main Thread: Newly started Instance exiting...");
                System.exit(1);
             }
            
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            try {
                conn.close();
                logger.info("Closing DB connection!");
            } catch (SQLException ex) {
                // TODO Auto-generated catch block
                ex.printStackTrace();
            } 
            logger.info("Main Thread: SQL Exception occurred! Newly started Instance exiting...");
            System.exit(1);
        }


        