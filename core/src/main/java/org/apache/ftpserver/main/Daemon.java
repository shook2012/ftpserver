/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */  

package org.apache.ftpserver.main;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.ftplet.FtpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.FileSystemResource;

/**
 * Invokes FtpServer as a daemon, running in the background. 
 * Used for example for the Windows service.
 */
public class Daemon {

    private static final Logger LOG = LoggerFactory.getLogger(Daemon.class);
    
    private static FtpServer server;
    private static Object lock = new Object();
    
    public static void main(String[] args) throws Exception {
        try{
            if(server == null) {
                // get configuration
                FtpServer server = getConfiguration(args);
                if(server == null) {
                    LOG.error("No configuration provided");
                    throw new FtpException("No configuration provided");
                }
            }
            
            String command = "start";
            
            if(args != null && args.length > 0) {
                command = args[0];
            }
            
            if(command.equals("start")) {
                LOG.info("Starting FTP server daemon");
                server.start();
                
                synchronized (lock) {
                    lock.wait();
                }
            } else if(command.equals("stop")) {
                synchronized (lock) {
                    lock.notify();
                }
                LOG.info("Stopping FTP server daemon");
                server.stop();
            }
        } catch(Throwable t) {
            LOG.error("Daemon error", t);
        }
    }

    /**
     * Get the configuration object.
     */
    private static FtpServer getConfiguration(String[] args) throws Exception {
    
        FtpServer server = null;
        if(args == null || args.length < 2) {
            LOG.info("Using default configuration....");
            server = new FtpServer();
        } else if( (args.length == 2) && args[1].equals("-default") ) {
            // supported for backwards compatibility, but not documented
            System.out.println("The -default switch is deprecated, please use --default instead");
            LOG.info("Using default configuration....");
            server = new FtpServer();
        } else if( (args.length == 2) && args[1].equals("--default") ) {
            LOG.info("Using default configuration....");
            server = new FtpServer();
        }
        else if( args.length == 2 ) {
            LOG.info("Using xml configuration file " + args[1] + "...");
            XmlBeanFactory bf = new XmlBeanFactory(new FileSystemResource(args[1]));
            if(bf.containsBean("server")) {
                server = (FtpServer) bf.getBean("server");
            } else {
                String[] beanNames = bf.getBeanNamesForType(FtpServer.class);
                if(beanNames.length == 1) {
                    server = (FtpServer) bf.getBean(beanNames[0]);
                } else if(beanNames.length > 1) {
                    System.out.println("Using the first server defined in the configuration, named " + beanNames[0]);
                    server = (FtpServer) bf.getBean(beanNames[0]);
                } else {
                    System.err.println("XML configuration does not contain a server configuration");
                }
            }
        } else {
            throw new FtpException("Invalid configuration option");
        }
        
        return server;
    }
}