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

package org.apache.ftpserver.usermanager;

import java.net.InetAddress;
import java.security.cert.Certificate;

/**
 * User metadata used during authentication
 */
public class UserMetadata {

    private Certificate[] certificateChain;

    private InetAddress inetAddress;

    /**
     * Retrive the certificate chain used for an SSL connection.
     * @return The certificate chain, can be null if no peer certificate
     *   is available (e.g. SSL not used)
     */
    public Certificate[] getCertificateChain() {
        return certificateChain;
    }

    /**
     * Set the certificate chain
     * @param certificateChain
     *            The certificate chain to set
     */
    public void setCertificateChain(Certificate[] certificateChain) {
        this.certificateChain = certificateChain;
    }

    /**
     * Retrive the remote IP adress of the client
     * @return The client IP adress
     */
    public InetAddress getInetAddress() {
        return inetAddress;
    }

    /**
     * Set the remote IP adress of the client
     * @param inetAddress
     *            The client IP adress
     */
    public void setInetAddress(InetAddress inetAddress) {
        this.inetAddress = inetAddress;
    }

}