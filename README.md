# Olingo-odata4-jpa-processor

The JPA Processor serves the process between Olingo V4 and the database, by providing a mapping with JPA metadata to OData metadata, satisfying OData requests by generating SQL queries and supporting the entity manipulations.

This work is inspired by the [SAP's JPA Processor](https://github.com/SAP/olingo-jpa-processor-v4) and [Apache Olingo OData 4.0 Java Library](https://github.com/apache/olingo-odata4).

The main module contains two Maven submodules:
* Olingo, a "capita selecta" for UI5 of the Olingo library. (Discarding e.g., geometrics)
* Sequoia, a library to construct a project Eclipse EE4J, subproject Jakarta Persistence, Jakarta Servlet.

For testing purpose EclipseLink is used as well as the Derby (directory "..\sequoia\odata4-jpa-processor\testdb") and HSQLDB (HyperSQL DataBase) (in memory) databases.