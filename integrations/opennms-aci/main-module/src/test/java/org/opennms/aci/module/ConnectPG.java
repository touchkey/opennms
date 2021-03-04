/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2017 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2017 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/
package org.opennms.aci.module;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
//import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ConnectPG {
	static Date today = new Date();
	
	static String message = "{\"code\":\"F120368\",\"subject\":\"counter\",\"ack\":\"no\",\"cause\":\"threshold-crossed\",\"rule\":\"tca-qosm-ingr-pkts5min-drop-rate\",\"dn\":\"subj-[topology\\/pod-1\\/node-125\\/sys\\/qosm\\/if-[eth1\\/52]\\/class-level3]\\/fr-12888860782\",\"type\":\"operational\",\"modTs\":\"never\",\"delegated\":\"no\",\"id\":\"12888860782\",\"prevSeverity\":\"warning\",\"ind\":\"deletion\",\"severity\":\"cleared\",\"origSeverity\":\"warning\",\"created\":\"113734369-03-28T02:32:48.496-06 BC\",\"occur\":\"2\",\"childAction\":\"\",\"delegatedFrom\":\"\",\"affected\":\"topology\\/pod-1\\/node-125\\/sys\\/qosm\\/if-[eth1\\/52]\\/class-level3\",\"highestSeverity\":\"warning\",\"changeSet\":\"dropRate:8\",\"descr\":\"TCA: ingress drop packets rate(qosmIngrPkts5min:dropRate) value 9 raised above threshold 9 and value is recovering \",\"domain\":\"infra\",\"lc\":\"\",\"status\":\"\"}";

	public static void main(String[] args) throws SQLException, ParseException, java.text.ParseException {
	 
	
        Connection conn = null;
        try {
        	
        	 String url = "jdbc:postgresql://localhost/opennms";
        	 String user = "postgres";
             String password = "postgres";
        	
            conn = DriverManager.getConnection(url, user, password);
            
            System.out.println("Connected to the PostgreSQL server successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        String query = " insert into events (eventid,eventuei,nodeid,eventtime,eventhost,eventsource,ipaddr,eventsnmphost,serviceid,eventsnmp,eventcreatetime,eventdescr,eventloggroup,eventlogmsg,eventseverity,eventpathoutage,eventcorrelation,eventsuppressedcount,eventoperinstruct,eventautoaction,eventoperaction,eventoperactionmenutext,eventnotification,eventtticket,eventtticketstate,eventforward,eventmouseovertext,eventlog,eventdisplay,eventackuser,eventacktime,alarmid,ifindex,systemid)"
                + " values ('112','uei.opennms.org/cisco/aci/F112128',null,?,'','ApicService','','',null,'',?,'','','','7','','',null,'','','','','','',null,'','','Y','Y','',null,null,null,'abc')";   
        	 
       PreparedStatement preparedStmt = conn.prepareStatement(query);
       //preparedStmt.setTimestamp(1, new Timestamp(today.getTime()));
       //preparedStmt.setTimestamp(2, new Timestamp(today.getTime()));
        preparedStmt.setTimestamp(1, new Timestamp(testDate().getTime()));
        preparedStmt.setTimestamp(2, new Timestamp(testDate().getTime()));

        
       // preparedStmt.execute();
        
        conn.close();
        
        ConnectPG connectpg = new ConnectPG();
        connectpg.testDate();
    }
	
	public static Date testDate() throws ParseException, java.text.ParseException {
        JSONParser parser = new JSONParser();

        JSONObject attributes = (JSONObject) parser.parse(message);
        Date createDate = null;
        String created = (String)attributes.get("created");      
        System.out.println(created.contains("T"));
        if ((created != null) && (created.contains("T"))) {
        try {
      	
        String[] startTimeparts = created.split("T");
        String onlydate = startTimeparts[0];
        String justDate[] = onlydate.split("-");
        String year = justDate[0];
        System.out.print(year); 
        if(year.length() == 4) {
        	String onlytimewtz = startTimeparts[1];
            String onlytime = onlytimewtz.substring(0, onlytimewtz.length() - 6);
            String onlytz = onlytimewtz.substring(onlytimewtz.length() - 6);
            String tz = onlytz.replace(":", "");

            createDate = ApicService.format.parse(onlydate + "T" + onlytime
                    + tz);
        }
        
       }
        catch (Throwable e) {
            }

	}
        createDate = null;
        if (createDate == null || createDate.after(today))
            createDate = today;
		return createDate;
		
		

	}
	
	
}

