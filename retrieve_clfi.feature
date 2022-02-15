Feature: GET Facility Event

  @tag4
  Scenario: 4 - Retrieve with clfi
    Given Delete Event from FacilityEvent and corresponding Jeopardies for <salesOrderNumber>
      | salesOrderNumber |
      | VU14000573       |
      | IP17002224       |
      | IP14000925       |
      | FR05244898       |
      | FR05208715       |
    And following FacilityEvent Exists and scheduled and some completed <eventName>, <salesOrderNumber>, <usoNumber>, <clfi>, <facilityOrderNumber>, <ecd>, <scheduledDate>, <completedDate> , <lastJeopCode>
      | eventName | salesOrderNumber | usoNumber | clfi                                 | facilityOrderNumber | ecd        | scheduledDate | completedDate | lastJeopCode |
      | FACRID    | IP17002224       |  33432877 | 100  ETSEL NSVLTNMT   NSVLTNMT92W    | W1400272            |            | 2017-12-01    |               |              |
      | FACRID    | IP14000925       |  33740866 | 5       ETS12 KSCYMO09   KSCYMO0907W | W1013776            |            | 2017-12-03    |               |              |
      | FACRID    | FR05244898       |  30740367 | 8954 T1F     MOBLALAZ   PNSCFLFP     | CG112233            |            | 2017-12-05    |               |              |
      | FACRID    | FR05208715       |  32402101 | 3055 TTT3   RCMDVAGRFD2RCMDVAITFD4   | SENTTC18            |            | 2017-12-01    |               |              |
      | FACRID    | VU14000573       |  33739915 | 483  ETSEL SNFCCA21   SNFCCA2117W    | W1013381            | 2017-11-05 | 2017-12-04    |               |              |
      | FACRID    | VU14000573       |  33739916 | 484  ETSEL SNFCCA21   SNFCCA2117W    | W1013383            |            | 2017-12-05    |               |              |
      | FACRID    | VU14000573       |  33739915 | 173  ETS192SNFCCA21   SNFCCA2117W    | W1013380            | 2017-12-01 | 2017-12-03    |               | C91          |
      | FACRID    | VU14000573       |  33739916 | 174  ETS192SNFCCA21   SNFCCA2117W    | W1013382            |            | 2017-12-10    |               | A94X         |
      | FACIE     | IP17002224       |  33432877 | 100  ETSEL NSVLTNMT   NSVLTNMT92W    | W1400272            |            | 2017-12-01    |               |              |
      | FACIE     | IP14000925       |  33740866 | 5       ETS12 KSCYMO09   KSCYMO0907W | W1013776            |            | 2017-12-03    |               |              |
      | FACIE     | FR05244898       |  30740367 | 8954 T1F     MOBLALAZ   PNSCFLFP     | CG112233            |            | 2017-12-05    |               |              |
      | FACIE     | FR05208715       |  32402101 | 3055 TTT3   RCMDVAGRFD2RCMDVAITFD4   | SENTTC18            |            | 2017-12-01    | 2017-11-29    |              |
      | FACIE     | VU14000573       |  33739915 | 483  ETSEL SNFCCA21   SNFCCA2117W    | W1013381            |            | 2017-11-05    |               |              |
      | FACIE     | VU14000573       |  33739916 | 484  ETSEL SNFCCA21   SNFCCA2117W    | W1013383            |            | 2017-12-05    | 2017-12-03    |              |
      | FACIE     | VU14000573       |  33739915 | 173  ETS192SNFCCA21   SNFCCA2117W    | W1013380            |            | 2017-12-03    |               |              |
      | FACIE     | VU14000573       |  33739916 | 174  ETS192SNFCCA21   SNFCCA2117W    | W1013382            |            | 2017-12-10    |               |              |
    And Jeopardy Exists for <eventName> and <facilityOrderNumber> For <jeopCode>, <description>, <note>, <status>, <statusUpdatedDate>, <CreatedDate>, <source> and <lastUpdatedBy>
      | eventName | facilityOrderNumber | jeopCode | description            | note                     | status | statusUpdatedDate | CreatedDate | source | lastUpdatedBy |
      | FACRID    | W1013380            | C91      | Cancel                 |                          | Closed | 2017-10-31        | 2017-10-31  | EFMS   | EFMS          |
      | FACRID    | W1013382            | Z63      | Test Description Value | Testing the note for Z63 | Closed | 2017-11-10        | 2017-11-05  | OSDS   | System        |
      | FACRID    | W1013382            | A94X     |                        |                          | Open   | 2017-11-15        | 2017-11-15  | EFMS   | EFMS          |
    When retrieveEvent for <clfi>
      | clfi                              |
      | 173  ETS192SNFCCA21   SNFCCA2117W |
    Then following FacilityEvents returned <eventName>, <salesOrderNumber>, <usoNumber>, <facilityOrderNumber>
      | eventName | salesOrderNumber | usoNumber | facilityOrderNumber |
      | FACRID    | VU14000573       |  33739915 | W1013380            |
      | FACIE     | VU14000573       |  33739915 | W1013380            |
    And following Jeopardies returned for <eventName>, <facilityOrderNumber> and <jeopCode>
      | eventName | facilityOrderNumber | jeopCode |
      | FACRID    | W1013380            | C91      |
