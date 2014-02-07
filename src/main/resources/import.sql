--
-- JBoss, Home of Professional Open Source
-- Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
-- contributors by the @authors tag. See the copyright.txt in the
-- distribution for a full listing of individual contributors.
--
-- Licensed under the Apache License, Version 2.0 (the "License");
-- you may not use this file except in compliance with the License.
-- You may obtain a copy of the License at
-- http://www.apache.org/licenses/LICENSE-2.0
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.
--

-- You can use this file to load seed data into the database using SQL statements
insert into Member (id, name, password, email, phone_number, dateModified, version) values (0, 'John Smith', '1234', 'john.smith@mailinator.com', '2125551212', now(), 0) 
insert into Source (id, url, prefix, suffix, latitude, longitude, idKey, parser, dateModified, version) values (0, 'http://www.domain.com.au/Search/buy/?nosurl=1&mode=buy&&state=NSW&areas=Blue+Mountains+%26+Surrounds%2cNorth+Shore+-+Upper%2cCanterbury%2fBankstown%2cNorthern+Beaches%2cEastern+Suburbs%2cNorthern+Suburbs%2cHawkesbury%2cParramatta%2cHills%2cSt+George%2cInner+West%2cSutherland%2cLiverpool+%2f+Fairfield%2cSydney+City%2cMacarthur%2fCamden%2cWestern+Sydney%2cNorth+Shore+-+Lower&displmap=1', 'DomainGoogleMapNameSpace.MapFunctionality("map-canvas",', ');', 'Latitude','Longtitude', 'AdIDs', 'com.ray.rema.parser.PropertyJsonParser',  now(), 0)