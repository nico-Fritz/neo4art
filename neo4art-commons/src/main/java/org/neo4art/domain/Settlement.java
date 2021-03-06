/**
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.neo4art.domain;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.neo4art.graphdb.Node;
import org.neo4j.graphdb.Label;

/**
 * @author Lorenzo Speranzoni
 * @since 4 Apr 2015
 */
public class Settlement implements Node {

  private static final Label[] LABELS = new Label[] { Neo4ArtLabel.Settlement };

  private Long                 nodeId;

  private String               name;
  private String               officialName;
  private String               nativeName;
  private String               otherName;
  private String               state;
  private String               country;
  
  private String               type;
  private URL                  website;
  
  private Coordinates          coordinates;

  public Settlement() {
  }

  public Settlement(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getOfficialName() {
    return officialName;
  }

  public void setOfficialName(String officialName) {
    this.officialName = officialName;
  }

  public String getNativeName() {
    return nativeName;
  }

  public void setNativeName(String nativeName) {
    this.nativeName = nativeName;
  }

  public String getOtherName() {
    return otherName;
  }

  public void setOtherName(String otherName) {
    this.otherName = otherName;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public URL getWebsite() {
    return website;
  }

  public void setWebsite(URL website) {
    this.website = website;
  }

  public Coordinates getCoordinates() {
    return coordinates;
  }

  public void setCoordinates(Coordinates coordinates) {
    this.coordinates = coordinates;
  }

  @Override
  public Long getNodeId() {
    return this.nodeId;
  }

  @Override
  public void setNodeId(long nodeId) {
    this.nodeId = nodeId;
  }

  @Override
  public Map<String, Object> getProperties() {
    Map<String, Object> properties = new HashMap<String, Object>();

    if (this.name != null) {
      properties.put("name", this.name);
    }
    if (this.officialName != null) {
      properties.put("officialName", this.officialName);
    }
    if (this.nativeName != null) {
      properties.put("nativeName", this.nativeName);
    }
    if (this.otherName != null) {
      properties.put("otherName", this.otherName);
    }
    if (this.type != null) {
      properties.put("type", this.type);
    }
    if (this.website != null) {
      properties.put("website", this.website.toString());
    }
    if (this.coordinates != null && this.getCoordinates().getStatus() == Coordinates.COMPUTED) {
      properties.put("lon", this.getCoordinates().getLongitude());
      properties.put("lat", this.getCoordinates().getLatitude());
    }

    return properties;
  }

  @Override
  public Label[] getLabels() {
    return LABELS;
  }
}
