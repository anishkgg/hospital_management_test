# Graph Report - hospital_management_test  (2026-06-30)

## Corpus Check
- 42 files · ~7,115 words
- Verdict: corpus is large enough that graph structure adds value.

## Summary
- 326 nodes · 653 edges · 21 communities (14 shown, 7 thin omitted)
- Extraction: 95% EXTRACTED · 5% INFERRED · 0% AMBIGUOUS · INFERRED: 32 edges (avg confidence: 0.8)
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

## God Nodes (most connected - your core abstractions)
1. `AppointmentResponseDTO` - 25 edges
2. `Doctor` - 22 edges
3. `controller` - 21 edges
4. `Appointment` - 21 edges
5. `DoctorDetailsResponseDTO` - 18 edges
6. `Hospital` - 16 edges
7. `AppointmentServiceImpl` - 15 edges
8. `AppointmentReportRepository` - 15 edges
9. `AppointmentRepository` - 14 edges
10. `DoctorResponseDTO` - 13 edges

## Surprising Connections (you probably didn't know these)
- `hospitalmanagementtest (debug)` --semantically_similar_to--> `hospitalmanagementtest`  [INFERRED] [semantically similar]
  compose.debug.yaml → compose.yaml
- `controller` --references--> `AppointmentService`  [EXTRACTED]
  src/main/java/com/hospital/Controller/controller.java → src/main/java/com/hospital/Service/AppointmentService.java
- `controller` --references--> `DoctorService`  [EXTRACTED]
  src/main/java/com/hospital/Controller/controller.java → src/main/java/com/hospital/Service/DoctorService.java
- `controller` --references--> `HospitalService`  [EXTRACTED]
  src/main/java/com/hospital/Controller/controller.java → src/main/java/com/hospital/Service/HospitalService.java
- `controller` --references--> `ReportService`  [EXTRACTED]
  src/main/java/com/hospital/Controller/controller.java → src/main/java/com/hospital/Service/ReportService.java

## Import Cycles
- None detected.

## Hyperedges (group relationships)
- **Container Deployment Configuration** — compose_debug_hospitalmanagementtest, compose_hospitalmanagementtest, dockerfile [EXTRACTED 1.00]
- **Graphify Agent Tooling** — agents_rules_graphify_rule, agents_workflows_graphify_workflow [EXTRACTED 1.00]

## Communities (21 total, 7 thin omitted)

### Community 0 - "Community 0"
Cohesion: 0.07
Nodes (32): IllegalArgumentException, Pattern, DoctorDetailsResponseDTO, Builder, LocalTime, Long, String, Doctor (+24 more)

### Community 1 - "Community 1"
Cohesion: 0.20
Nodes (10): GetMapping, PostMapping, PutMapping, RequestMapping, RestController, controller, List, Long (+2 more)

### Community 2 - "Community 2"
Cohesion: 0.19
Nodes (10): Optional, SecureRandom, AppointmentRepository, LocalDateTime, Long, Query, Repository, String (+2 more)

### Community 3 - "Community 3"
Cohesion: 0.08
Nodes (28): ApplicationEventPublisher, AppointmentCompleteRequestDTO, String, AppointmentRescheduleRequestDTO, LocalDateTime, AppointmentResponseDTO, Builder, LocalDateTime (+20 more)

### Community 4 - "Community 4"
Cohesion: 0.11
Nodes (25): Pageable, AppointmentBookingResponseDTO, Builder, LocalDateTime, String, Appointment, AllArgsConstructor, Builder (+17 more)

### Community 5 - "Community 5"
Cohesion: 0.14
Nodes (12): Double, AppointmentRequestDTO, LocalDateTime, Long, String, HospitalReportDTO, Builder, Long (+4 more)

### Community 6 - "Community 6"
Cohesion: 0.16
Nodes (13): HospitalRequestDTO, Builder, String, HospitalResponseDTO, Builder, Long, String, HospitalService (+5 more)

### Community 7 - "Community 7"
Cohesion: 0.12
Nodes (16): DoctorRegistrationDTO, Builder, LocalTime, Long, String, DoctorRequestDTO, Builder, Long (+8 more)

### Community 8 - "Community 8"
Cohesion: 0.36
Nodes (4): Connection, Properties, DatabaseConnection, String

### Community 9 - "Community 9"
Cohesion: 0.46
Nodes (5): Async, Component, EventListener, Logger, NotificationEventListener

### Community 10 - "Community 10"
Cohesion: 0.39
Nodes (6): ExceptionHandler, Map, RestControllerAdvice, GlobalExceptionHandler, ResponseEntity, String

### Community 11 - "Community 11"
Cohesion: 0.47
Nodes (4): EnableAsync, SpringBootApplication, HospitalManagementApplication, String

### Community 19 - "Community 19"
Cohesion: 0.13
Nodes (11): drawAnalyticsChart(), fetchAppointments(), fetchDoctors(), fetchHospitals(), formatDateTime(), loadAllData(), loadDashboardData(), renderDashboardShifts() (+3 more)

### Community 20 - "Community 20"
Cohesion: 0.13
Nodes (16): JpaRepository, Hospital, AllArgsConstructor, Builder, Data, Entity, List, Long (+8 more)

## Knowledge Gaps
- **7 isolated node(s):** `com.hospital:hospital-management-test`, `state`, `graphify`, `Workflow: graphify`, `My Project` (+2 more)
  These have ≤1 connection - possible missing edges or undocumented components.
- **7 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `Doctor` connect `Community 0` to `Community 3`, `Community 4`, `Community 20`?**
  _High betweenness centrality (0.121) - this node is a cross-community bridge._
- **Why does `AppointmentResponseDTO` connect `Community 3` to `Community 1`, `Community 4`, `Community 5`?**
  _High betweenness centrality (0.089) - this node is a cross-community bridge._
- **Why does `Appointment` connect `Community 4` to `Community 0`, `Community 2`, `Community 3`?**
  _High betweenness centrality (0.079) - this node is a cross-community bridge._
- **What connects `com.hospital:hospital-management-test`, `state`, `graphify` to the rest of the system?**
  _7 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `Community 0` be split into smaller, more focused modules?**
  _Cohesion score 0.07294117647058823 - nodes in this community are weakly interconnected._
- **Should `Community 3` be split into smaller, more focused modules?**
  _Cohesion score 0.08233117483811286 - nodes in this community are weakly interconnected._
- **Should `Community 4` be split into smaller, more focused modules?**
  _Cohesion score 0.10756302521008404 - nodes in this community are weakly interconnected._