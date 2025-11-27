<!DOCTYPE html>
<html lang="en">
<body>
<main>

  <h1>🚀Microservices with Spring Boot, Docker, Kubernetes</h1>
  <p class="tagline">
    A complete, production-grade microservices architecture using Spring Boot,
    Spring Cloud, Docker, Kubernetes, Helm, Istio, mTLS, Keycloak, RabbitMQ,
    Kafka, and a full observability stack (Grafana, Prometheus, Loki, Tempo, Alloy).
  </p>

  <h2>📌 Table of Contents</h2>
  <ul>
    <li><a href="#overview">Overview</a></li>
    <li><a href="#architecture">Architecture</a></li>
    <li><a href="#technologies">Technologies</a></li>
    <li><a href="#services">Services</a></li>
    <li><a href="#cloud-native-components">Cloud-Native Components</a></li>
    <li><a href="#security-with-keycloak">Security with Keycloak</a></li>
    <li><a href="#messaging">Messaging (RabbitMQ &amp; Kafka)</a></li>
    <li><a href="#service-mesh-mtls">Service Mesh &amp; mTLS</a></li>
    <li><a href="#observability">Observability Stack</a></li>
    <li><a href="#kubernetes-helm">Kubernetes &amp; Helm</a></li>
    <li><a href="#build-run">Build &amp; Run</a></li>
    <li><a href="#folder-structure">Folder Structure</a></li>
    <li><a href="#credits">Acknowledgements</a></li>
  </ul>

  <h2 id="overview">🧩 Overview</h2>
  <p>
    This repository demonstrates an end-to-end microservices ecosystem inspired by a
    banking domain. It includes three core domain services (Accounts, Cards, Loans)
    and multiple supporting infrastructure services such as API Gateway, Service
    Registry, Config Server, Messaging (RabbitMQ &amp; Kafka), Observability, Security,
    and Service Mesh.
  </p>
  <p>
    The goal is to showcase how modern cloud-native applications are built, secured,
    deployed, and observed in a Kubernetes environment.
  </p>

  <h2 id="architecture">🏛 Architecture</h2>
  <p>The high-level flow of the system:</p>
  <pre><code>                         ┌─────────────────────────────┐
                         │         Client              │
                         └────────────┬────────────────┘
                                      │
                                      ▼
                      ┌─────────────────────────────────────┐
                      │ API Gateway                         │
                      │ (Spring Cloud Gateway + Keycloak)   │
                      └────────────────┬────────────────────┘
                                       │
                                       ▼
                        [ Service Mesh (Istio, Envoy, mTLS) ]
                                       │
           ┌───────────────────────────┼───────────────────────────┐
           │                           │                           │
           ▼                           ▼                           ▼
┌───────────────────┐      ┌───────────────────┐      ┌────────────────────┐
│ Accounts Service  │      │ Cards Service     │      │ Loans Service      │
└─────────┬─────────┘      └─────────┬─────────┘      └──────────┬─────────┘
          │                           │                           │
          ▼                           ▼                           ▼
   ┌──────────────┐           ┌──────────────┐             ┌──────────────┐
   │ Accounts DB  │           │ Cards DB     │             │ Loans DB     │
   └──────────────┘           └──────────────┘             └──────────────┘


           ┌───────────────────────────────┐
           │   Message / Integration MS    │
           └───────────────┬───────────────┘
                           │
                           ▼
                  ┌───────────────────┐
                  │ RabbitMQ / Kafka  │
                  └───────────────────┘


                 ┌───────────────────────────────────────────────┐
                 │          Observability Stack                  │
                 │ Prometheus, Grafana, Loki, Tempo, Alloy ...   │
                 │  - Takes metrik/log/trace from gateway        │
                 │  - Takes all data from microservice + mesh    │
                 │  - Takes data from DB/MQ nodes if necessary   │
                 └───────────────────────────────────────────────┘
</code></pre>

  <h2 id="technologies">🛠 Technologies</h2>

  <h3>Backend</h3>
  <ul class="pill-list">
    <li>Java 21+</li>
    <li>Spring Boot 3.x</li>
    <li>Spring Web</li>
    <li>Spring Data JPA</li>
    <li>Spring Security</li>
    <li>Spring Cloud Gateway</li>
    <li>Spring Cloud Config</li>
    <li>Spring Cloud Eureka</li>
    <li>Resilience4j</li>
  </ul>

  <h3>Messaging</h3>
  <ul class="pill-list">
    <li>RabbitMQ</li>
    <li>Apache Kafka</li>
  </ul>

  <h3>Security</h3>
  <ul class="pill-list">
    <li>Keycloak (OIDC, OAuth2)</li>
    <li>JWT Resource Servers</li>
    <li>Istio mTLS</li>
    <li>Zero Trust principles</li>
  </ul>

  <h3>Containers &amp; Orchestration</h3>
  <ul class="pill-list">
    <li>Docker</li>
    <li>Docker Compose</li>
    <li>Kubernetes</li>
    <li>NGINX Ingress Controller</li>
    <li>Helm Charts</li>
  </ul>

  <h3>Service Mesh</h3>
  <ul class="pill-list">
    <li>Istio</li>
    <li>Envoy sidecars</li>
    <li>Strict mTLS</li>
    <li>Traffic management</li>
  </ul>

  <h3>Observability</h3>
  <ul class="pill-list">
    <li>Prometheus (metrics)</li>
    <li>Grafana (dashboards)</li>
    <li>Loki (logs)</li>
    <li>Tempo (traces)</li>
    <li>Alloy (agent / collector)</li>
  </ul>

  <h3>Build &amp; Dependency Management</h3>
  <ul class="pill-list">
    <li>Maven</li>
    <li>Spring Boot BOM (Bill of Materials)</li>
  </ul>

  <h2 id="services">🧱 Microservices</h2>
  <table>
    <thead>
      <tr>
        <th>Service</th>
        <th>Description</th>
      </tr>
    </thead>
    <tbody>
      <tr>
        <td><code>accounts</code></td>
        <td>Accounts microservice responsible for account-related operations.</td>
      </tr>
      <tr>
        <td><code>cards</code></td>
        <td>Cards microservice responsible for card management operations.</td>
      </tr>
      <tr>
        <td><code>loans</code></td>
        <td>Loans microservice responsible for loan-related workflows.</td>
      </tr>
      <tr>
        <td><code>message</code></td>
        <td>Event-driven processing and asynchronous integration.</td>
      </tr>
      <tr>
        <td><code>gatewayserver</code></td>
        <td>Spring Cloud Gateway acting as the single entry point (API Gateway).</td>
      </tr>
      <tr>
        <td><code>eurekaserver</code></td>
        <td>Service Registry using Netflix Eureka for service discovery.</td>
      </tr>
      <tr>
        <td><code>configserver</code></td>
        <td>Centralized configuration using Spring Cloud Config.</td>
      </tr>
      <tr>
        <td><code>kiwix-bom</code></td>
        <td>Maven BOM module that centralizes dependency versions across services.</td>
      </tr>
    </tbody>
  </table>

  <h2 id="cloud-native-components">☁️ Cloud-Native Components</h2>
  <ul>
    <li><strong>Config Server</strong>: Externalizes configuration and exposes it over HTTP.</li>
    <li><strong>Eureka Server</strong>: Provides service discovery and registration.</li>
    <li><strong>Gateway</strong>: Handles routing, security, rate limiting, and cross-cutting concerns.</li>
    <li><strong>Resilience4j</strong>: Adds circuit breakers, retries, and rate-limiting for robustness.</li>
  </ul>

  <h2 id="security-with-keycloak">🔐 Security with Keycloak</h2>
  <p>
    Keycloak is used as the identity provider (IdP) for authentication and authorization.
    The API Gateway is configured as an OAuth2 client, while each microservice behaves
    as a resource server validating JWT tokens issued by Keycloak.
  </p>
  <ul>
    <li>Supports OAuth2 Authorization Code Flow.</li>
    <li>JWT validation in resource servers.</li>
    <li>Role/permission-based access control at the gateway and service level.</li>
    <li>Seamless integration with Istio and mTLS for in-cluster security.</li>
  </ul>

  <h2 id="messaging">📨 Messaging (RabbitMQ &amp; Kafka)</h2>
  <h3>RabbitMQ</h3>
  <ul>
    <li>Implements simple event publishing and point-to-point messaging.</li>
    <li>Used for work queues and fan-out scenarios.</li>
  </ul>
  <h3>Apache Kafka</h3>
  <ul>
    <li>Implements distributed event streaming.</li>
    <li>High-throughput producers and consumers.</li>
    <li>Replayable events and decoupled services.</li>
  </ul>

  <h2 id="service-mesh-mtls">🔐 Service Mesh &amp; mTLS</h2>
  <p>
    Istio provides a dedicated service mesh layer on top of the Kubernetes cluster.
    Each microservice pod includes an Envoy sidecar that intercepts all inbound and
    outbound traffic.
  </p>
  <ul>
    <li>Strict mTLS between all services.</li>
    <li>Sidecar-based traffic interception and control.</li>
    <li>Automatic certificate issuance and rotation via Istio control plane.</li>
    <li>Traffic management, retries, timeouts, circuit breaking at the mesh level.</li>
  </ul>
  <p>Typical flow between two services:</p>
  <ol>
    <li>Service A sends plain HTTP to its sidecar (Envoy).</li>
    <li>Envoy sidecar initiates mTLS handshake with Service B's sidecar.</li>
    <li>Certificates are validated by the mesh CA.</li>
    <li>Encrypted traffic flows sidecar-to-sidecar.</li>
    <li>Service B receives decrypted HTTP from its sidecar.</li>
  </ol>

  <h2 id="observability">📊 Observability Stack</h2>
  <p>
    The system exposes full observability capabilities using the "three pillars":
    metrics, logs, and traces.
  </p>

  <h3>Prometheus</h3>
  <ul>
    <li>Scrapes metrics from microservices, gateway, and mesh.</li>
    <li>Integrates with Spring Boot Actuator (<code>/actuator/prometheus</code>).</li>
  </ul>

  <h3>Grafana</h3>
  <ul>
    <li>Central dashboard for metrics, logs, and traces.</li>
    <li>Connects to Prometheus, Loki, and Tempo as data sources.</li>
  </ul>

  <h3>Loki</h3>
  <ul>
    <li>Stores and indexes logs from all microservices.</li>
    <li>Log queries and correlation through Grafana.</li>
  </ul>

  <h3>Tempo</h3>
  <ul>
    <li>Collects distributed traces from microservices.</li>
    <li>Shows end-to-end request flows across services.</li>
  </ul>

  <h3>Alloy</h3>
  <ul>
    <li>Agent/collector that ships metrics and logs to Prometheus and Loki.</li>
    <li>Runs alongside services or as a DaemonSet in Kubernetes.</li>
  </ul>

  <h2 id="kubernetes-helm">☸️ Kubernetes &amp; Helm</h2>
  <p>
    All microservices are packaged as Docker images and deployed onto a Kubernetes
    cluster. Helm is used to template Kubernetes manifests and manage releases.
  </p>
  <ul>
    <li>Deployments and Services for each microservice.</li>
    <li>ConfigMaps and Secrets for externalized configuration.</li>
    <li>Ingress resources for external HTTP(S) access.</li>
    <li>Helm charts in <code>helm-new/</code> for parameterized deployments.</li>
  </ul>

  <h2 id="folder-structure">📂 Folder Structure</h2>
  <pre><code>.
├── accounts
├── cards
├── loans
├── message
├── gatewayserver
├── eurekaserver
├── configserver
├── kiwix-bom
├── kubernetes/
├── helm-new/
├── docker-compose/
└── README.html</code></pre>

<h2 id="credits">🙏 Acknowledgements</h2>
<p>
  Special thanks to <strong>ezybytes</strong> for providing high-quality learning materials,
  architectural guidance, and hands-on resources that greatly inspired the
  structure and implementation of this microservices ecosystem.
</p>
<p>
  Their practical approach to modern software engineering concepts; microservices,
  DevOps, Kubernetes, cloud-native design, observability, and distributed systems
  helped shape many of the ideas included in this repository.
</p>
<p>
  If you're looking to deepen your knowledge in large-scale backend systems,
  microservices, and cloud-native application development, his content is
  absolutely worth following.
</p>

</main>
</body>
</html>
