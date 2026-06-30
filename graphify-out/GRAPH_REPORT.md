# Graph Report - hospital_management_test  (2026-07-01)

## Corpus Check
- 49 files · ~10,282 words
- Verdict: corpus is large enough that graph structure adds value.

## Summary
- 411 nodes · 808 edges · 22 communities (14 shown, 8 thin omitted)
- Extraction: 95% EXTRACTED · 5% INFERRED · 0% AMBIGUOUS · INFERRED: 39 edges (avg confidence: 0.8)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `15e6982e`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- [[_COMMUNITY_Community 0|Community 0]]
- [[_COMMUNITY_Community 1|Community 1]]
- [[_COMMUNITY_Community 2|Community 2]]
- [[_COMMUNITY_Community 3|Community 3]]
- [[_COMMUNITY_Community 4|Community 4]]
- [[_COMMUNITY_Community 5|Community 5]]
- [[_COMMUNITY_Community 6|Community 6]]
- [[_COMMUNITY_Community 7|Community 7]]
- [[_COMMUNITY_Community 8|Community 8]]
- [[_COMMUNITY_Community 9|Community 9]]
- [[_COMMUNITY_Community 10|Community 10]]
- [[_COMMUNITY_Community 11|Community 11]]
- [[_COMMUNITY_Community 12|Community 12]]
- [[_COMMUNITY_Community 13|Community 13]]
- [[_COMMUNITY_Community 14|Community 14]]
- [[_COMMUNITY_Community 15|Community 15]]
- [[_COMMUNITY_Community 16|Community 16]]
- [[_COMMUNITY_Community 17|Community 17]]
- [[_COMMUNITY_Community 18|Community 18]]
- [[_COMMUNITY_Community 19|Community 19]]
- [[_COMMUNITY_Community 20|Community 20]]
- [[_COMMUNITY_Community 21|Community 21]]

## God Nodes (most connected - your core abstractions)
1. `AppointmentResponseDTO` - 25 edges
2. `Doctor` - 23 edges
3. `controller` - 21 edges
4. `Appointment` - 21 edges
5. `DoctorDetailsResponseDTO` - 19 edges
6. `Hospital` - 17 edges
7. `AppointmentServiceImpl` - 15 edges
8. `User` - 15 edges
9. `AppointmentReportRepository` - 15 edges
10. `DoctorResponseDTO` - 14 edges

## Surprising Connections (you probably didn't know these)
- `hospitalmanagementtest (debug)` --semantically_similar_to--> `hospitalmanagementtest`  [INFERRED] [semantically similar]
  compose.debug.yaml → compose.yaml
- `controller` --references--> `DoctorService`  [EXTRACTED]
  src/main/java/com/hospital/Controller/controller.java → src/main/java/com/hospital/Service/DoctorService.java
- `controller` --references--> `HospitalService`  [EXTRACTED]
  src/main/java/com/hospital/Controller/controller.java → src/main/java/com/hospital/Service/HospitalService.java
- `controller` --references--> `ReportService`  [EXTRACTED]
  src/main/java/com/hospital/Controller/controller.java → src/main/java/com/hospital/Service/ReportService.java
- `AppointmentResponseDTO` --references--> `AppointmentStatus`  [EXTRACTED]
  src/main/java/com/hospital/dto/responseDto/AppointmentResponseDTO.java → src/main/java/com/hospital/Enum/AppointmentStatus.java

## Import Cycles
- None detected.

## Hyperedges (group relationships)
- **Container Deployment Configuration** — compose_debug_hospitalmanagementtest, compose_hospitalmanagementtest, dockerfile [EXTRACTED 1.00]
- **Graphify Agent Tooling** — agents_rules_graphify_rule, agents_workflows_graphify_workflow [EXTRACTED 1.00]

## Communities (22 total, 8 thin omitted)

### Community 0 - "Community 0"
Cohesion: 0.07
Nodes (36): DoctorRegistrationDTO, Builder, Double, LocalTime, Long, String, DoctorRequestDTO, Builder (+28 more)

### Community 1 - "Community 1"
Cohesion: 0.07
Nodes (38): CommandLineRunner, HttpServletRequest, DataInitializer, Component, Override, PasswordEncoder, AuthController, AuthenticationManager (+30 more)

### Community 2 - "Community 2"
Cohesion: 0.19
Nodes (10): SecureRandom, AppointmentRepository, LocalDateTime, Long, Optional, Query, Repository, String (+2 more)

### Community 3 - "Community 3"
Cohesion: 0.08
Nodes (27): PutMapping, controller, GetMapping, List, Long, PostMapping, RequestMapping, ResponseEntity (+19 more)

### Community 4 - "Community 4"
Cohesion: 0.08
Nodes (33): Pageable, AppointmentBookingResponseDTO, Builder, LocalDateTime, String, HospitalReportDTO, Builder, Double (+25 more)

### Community 5 - "Community 5"
Cohesion: 0.16
Nodes (13): Doctor, AllArgsConstructor, Builder, Data, Double, Entity, List, LocalTime (+5 more)

### Community 6 - "Community 6"
Cohesion: 0.11
Nodes (19): ApplicationEventPublisher, IllegalArgumentException, Pattern, AppointmentBookedEvent, LocalDateTime, Long, String, AppointmentCancelledEvent (+11 more)

### Community 7 - "Community 7"
Cohesion: 0.28
Nodes (9): AuthenticationConfiguration, Bean, Configuration, EnableWebSecurity, HttpSecurity, SecurityFilterChain, AuthenticationManager, PasswordEncoder (+1 more)

### Community 8 - "Community 8"
Cohesion: 0.36
Nodes (4): Connection, Properties, DatabaseConnection, String

### Community 9 - "Community 9"
Cohesion: 0.46
Nodes (5): Async, EventListener, Logger, Component, NotificationEventListener

### Community 10 - "Community 10"
Cohesion: 0.39
Nodes (6): ExceptionHandler, Map, RestControllerAdvice, GlobalExceptionHandler, ResponseEntity, String

### Community 11 - "Community 11"
Cohesion: 0.47
Nodes (4): EnableAsync, SpringBootApplication, HospitalManagementApplication, String

### Community 19 - "Community 19"
Cohesion: 0.11
Nodes (23): applyViewRestrictions(), checkAuthStatus(), closeModal(), drawAnalyticsChart(), fetchAppointments(), fetchDoctors(), fetchHospitals(), formatDateTime() (+15 more)

### Community 20 - "Community 20"
Cohesion: 0.07
Nodes (32): JpaRepository, HospitalRequestDTO, Builder, Double, String, HospitalResponseDTO, Builder, Double (+24 more)

## Knowledge Gaps
- **8 isolated node(s):** `com.hospital:hospital-management-test`, `state`, `graphify`, `Workflow: graphify`, `My Project` (+3 more)
  These have ≤1 connection - possible missing edges or undocumented components.
- **8 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `UserRepository` connect `Community 1` to `Community 20`?**
  _High betweenness centrality (0.110) - this node is a cross-community bridge._
- **Why does `Doctor` connect `Community 5` to `Community 0`, `Community 4`, `Community 20`, `Community 6`?**
  _High betweenness centrality (0.096) - this node is a cross-community bridge._
- **Why does `AppointmentResponseDTO` connect `Community 3` to `Community 4`, `Community 6`?**
  _High betweenness centrality (0.074) - this node is a cross-community bridge._
- **What connects `com.hospital:hospital-management-test`, `state`, `graphify` to the rest of the system?**
  _8 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `Community 0` be split into smaller, more focused modules?**
  _Cohesion score 0.06516290726817042 - nodes in this community are weakly interconnected._
- **Should `Community 1` be split into smaller, more focused modules?**
  _Cohesion score 0.07183673469387755 - nodes in this community are weakly interconnected._
- **Should `Community 3` be split into smaller, more focused modules?**
  _Cohesion score 0.08200290275761973 - nodes in this community are weakly interconnected._