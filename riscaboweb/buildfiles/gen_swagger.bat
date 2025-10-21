@rem ***************************************************************************
@rem  ========================LICENSE_START=================================
@rem   
@rem  Copyright (C) 2025 Regione Piemonte
@rem   
@rem  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
@rem  SPDX-License-Identifier: EUPL-1.2
@rem  =========================LICENSE_END==================================
@rem ***************************************************************************
REM La directory dove sono presenti i jar del generatore csi-swagger
set CSI_SWAGGER_GEN_HOME=c:\devtools\csi-swagger-codegen

set CLI_JAR=%CSI_SWAGGER_GEN_HOME%\swagger-codegen-cli.jar
set CUSTOM_GEN_JAR=%CSI_SWAGGER_GEN_HOME%\csi-java-swagger-codegen-1.1.0.001.jar

set SWAGGER_CP=%CLI_JAR%;%CUSTOM_GEN_JAR%

REM impostare solo per debug del generatore
set DEBUG_OPTS=
rem set DEBUG_OPTS=-Xdebug -Xrunjdwp:transport=dt_socket,address=9797,server=y,suspend=y

rem pause "#### generazione skeleton jaxrs ####"
java -cp %SWAGGER_CP% %DEBUG_OPTS% io.swagger.codegen.SwaggerCodegen generate -i ..\src\yaml\lgspa.yaml -l jaxrs-resteasy-eap-csi -o .. --config swagger_config_java.json

rem pause "#### generazione client jaxrs petstore ####"
java -cp %SWAGGER_CP% %DEBUG_OPTS% io.swagger.codegen.SwaggerCodegen generate -i ..\src\yaml\swagger_petstore_v2.yaml -l jaxrs-resteasy-eap-csi -o .. --config swagger_config_java_petstore_client.json

rem echo
rem pause "#### generazione stub angular2 ####"
rem java -cp %SWAGGER_CP% %DEBUG_OPTS% io.swagger.codegen.SwaggerCodegen generate -i ..\src\yaml\lgspa.yaml -l typescript-angular -o ..\..\lgspaapi --config swagger_config_angular.json 

rem echo
rem pause "#### generazione documentazione html ####"
rem java -jar %SWAGGER_CP% generate -l html -i ..\src\yaml\anagrafica.yaml -o ..\temphelp

rem pause "#### generazione stub ####" 
rem java -cp %SWAGGER_CP% %DEBUG_OPTS% io.swagger.codegen.SwaggerCodegen generate -i ..\src\yaml\lgspa.yaml -l java -o ..\..\lgspaapi --config swagger_config_java.json