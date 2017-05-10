# Lunatech

### Dev

Tomee (port 8080) (TODO : hot reload config)
```
mvn clean install tomee:run
```

UI with auto reload (port 8000)
```
npm run dev
```

### Build & Run

Package everything as a java webapp and run it with tomee.

Run :
```
mvn clean && npm install && npm run build && mvn install tomee:run -PwithUI -DskipTests=true
```
Go to `http://localhost:8080`

If you don't have `npm` then remove npm command from above command line, js packaged bundle is saved in the repo!

### Done

-  tests
-  React async single page UI
-  init dictionaries using flyway
-  container managed db
-  live filtering in ui

### Remaining

-  use JSON-API serializer to avoid multiple serialization of same entity (like country)
   - check for JOIN FETCH with FetchType.LAZY vs L2 cache then
-  init Schema using flyway & add index on t_surface
-  package everything (docker stack ?)
-  async JAX-RS endpoints
-  Evaluate ElasticSearch
-  Styling of the UI
-  Better merge build for UI and Server
-  IT tests using docker stack
-  Tomee hot reload dev config
-  Parameterized server scheme://host:port

A lot of answers depends on client context...

### Why EclipseLink (vs Hibernate)

- Fluent UNION
- Awesome composite Id support

### Convert to SQL script

```
sed "s/'/''/g" < countries.csv | gawk -vFPAT='[^,]*|"[^"]*"' 'NR>1 { printf "INSERT INTO T_COUNTRY (CODE,NAME) VALUES (\x27%s\x27,\x27%s\x27);",$2,$3;print ""}' | sed 's/"//g' > sql/V1.1__countries.sql
sed "s/'/''/g" < airports.csv | gawk -vFPAT='[^,]*|"[^"]*"' 'NR>1 { printf "INSERT INTO T_AIRPORT (ID,IDENT,CODE,NAME,COUNTRY_CODE) VALUES (%s,\x27%s\x27,\x27%s\x27,\x27%s\x27,\x27%s\x27);",$1,$2,$3,$4,$9;print ""}' | sed 's/"//g' > sql/V1.2__airports.sql
sed "s/'/''/g" < runways.csv | gawk -vFPAT='[^,]*|"[^"]*"' 'NR>1 { printf "INSERT INTO T_RUNWAY (ID,AIRPORT_ID,SURFACE,IDENT) VALUES (%s,\x27%s\x27,\x27%s\x27,\x27%s\x27);",$1,$2,$6,$9;print ""}' | sed 's/"//g' > sql/V1.3__runways.sql
```
