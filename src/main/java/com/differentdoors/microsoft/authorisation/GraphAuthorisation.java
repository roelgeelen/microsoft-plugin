package com.differentdoors.microsoft.authorisation;

import com.microsoft.aad.msal4j.ClientCredentialFactory;
import com.microsoft.aad.msal4j.ClientCredentialParameters;
import com.microsoft.aad.msal4j.ConfidentialClientApplication;
import com.microsoft.aad.msal4j.IAuthenticationResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

@Repository
public class GraphAuthorisation {
    @Value("${different_doors.microsoft.client_id}")
    private String clientId;

    @Value("${different_doors.microsoft.key_path}")
    private String keyPath;

    @Value("${different_doors.microsoft.cert_path}")
    private String certPath;

    @Value("${different_doors.microsoft.authority}")
    private String authority;

    @Value("${different_doors.microsoft.scope}")
    private String scope;

    private static ConfidentialClientApplication app;

    public void setGraphAuthorisation() throws Exception {
        InputStream iskey = GraphAuthorisation.class.getClassLoader().getResourceAsStream(keyPath);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(iskey.readAllBytes());
        PrivateKey key = KeyFactory.getInstance("RSA").generatePrivate(spec);

        InputStream iscert = GraphAuthorisation.class.getClassLoader().getResourceAsStream(certPath);
        InputStream certStream = new ByteArrayInputStream(iscert.readAllBytes());
        X509Certificate cert = (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(certStream);

        app = ConfidentialClientApplication.builder(
                        clientId,
                        ClientCredentialFactory.createFromCertificate(key, cert))
                .authority(authority)
                .build();
    }

    public IAuthenticationResult getAccessTokenByClientCredentialGrant() throws Exception {
        // With client credentials flows the scope is ALWAYS of the shape "resource/.default", as the
        // application permissions need to be set statically (in the portal), and then granted by a tenant administrator

        ClientCredentialParameters clientCredentialParam = ClientCredentialParameters.builder(
                        Collections.singleton(scope))
                .build();

        CompletableFuture<IAuthenticationResult> future = app.acquireToken(clientCredentialParam);
        return future.get();
    }
}
