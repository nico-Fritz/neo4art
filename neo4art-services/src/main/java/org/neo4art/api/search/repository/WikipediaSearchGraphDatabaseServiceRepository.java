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

package org.neo4art.api.search.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.neo4art.api.search.bean.WikipediaSearchResult;
import org.neo4art.api.search.bean.WikipediaSearchResultNode;
import org.neo4art.api.search.bean.WikipediaSearchResultRelationship;
import org.neo4art.graphdb.connection.GraphDatabaseConnectionManager;
import org.neo4art.graphdb.connection.GraphDatabaseConnectionManagerFactory;
import org.neo4art.graphdb.connection.GraphDatabaseConnectionManagerFactory.GraphDatabaseConnectionType;
import org.neo4art.graphdb.transaction.GraphDatabaseTransaction;
import org.neo4art.importer.wikipedia.graphdb.WikipediaLabel;
import org.neo4art.importer.wikipedia.graphdb.WikipediaRelationship;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Result;
import org.neo4j.helpers.collection.MapUtil;

/**
 * @author Lorenzo Speranzoni
 * @since 31 Oct 2015
 */
public class WikipediaSearchGraphDatabaseServiceRepository implements WikipediaSearchRepository {

  /**
   * @see org.neo4art.api.search.repository.WikipediaSearchRepository#findDepthOneConnectionsByPageTitle(java.lang.String, boolean)
   */
  @Override
  public WikipediaSearchResult findDepthOneConnectionsByPageTitle(String wikipediaPageTitle, boolean autoComplete) {

    String cql = "MATCH (n:" + WikipediaLabel.Wikipedia + ")-[r:" + WikipediaRelationship.REFERS + "]-(m:" + WikipediaLabel.Wikipedia + ") WHERE n.title = {title} RETURN n, r, m";

    Map<String, Object> parameters = MapUtil.map("title", wikipediaPageTitle);

    return executeQuery(cql, parameters, autoComplete);
  }

  /**
   * @see org.neo4art.api.search.repository.WikipediaSearchRepository#findDepthOneConnectionsByNodeId(long, boolean)
   */
  @Override
  public WikipediaSearchResult findDepthOneConnectionsByNodeId(long nodeId, boolean autoComplete) {

    String cql = "MATCH (n:" + WikipediaLabel.Wikipedia + ")-[r:" + WikipediaRelationship.REFERS + "]-(m:" + WikipediaLabel.Wikipedia + ") WHERE id(n) = {nodeId} RETURN n, r, m";

    Map<String, Object> parameters = MapUtil.map("nodeId", nodeId);

    return executeQuery(cql, parameters, autoComplete);
  }

  /**
   * 
   * @param query
   * @param parameters
   * @param autoComplete
   * @return
   */
  private WikipediaSearchResult executeQuery(String query, Map<String, Object> parameters, boolean autoComplete) {

    WikipediaSearchResult result = new WikipediaSearchResult();

    GraphDatabaseConnectionManager graphDatabaseConnectionManager = GraphDatabaseConnectionManagerFactory.getInstance(GraphDatabaseConnectionType.EMBEDDED_DATABASE);

    Map<Long, Integer> nodeIds = new HashMap<Long, Integer>();
    Map<Long, Integer> relationshipIds = new HashMap<Long, Integer>();

    try (GraphDatabaseTransaction tx = graphDatabaseConnectionManager.getTransactionManager()) {

      Result queryResult = graphDatabaseConnectionManager.executeCypherQuery(query, parameters);

      boolean first = true;

      while (queryResult.hasNext()) {

        Map<String, Object> row = queryResult.next();

        for (Entry<String, Object> column : row.entrySet()) {

          if ("n".equals(column.getKey())) {

            if (first) {

              WikipediaSearchResultNode node = createNode((Node) column.getValue());

              if (nodeIds.get(node.getId()) == null) {

                nodeIds.put(node.getId(), nodeIds.size());
                result.addNode(node);
                first = false;
              }
            }
          }
          else if ("m".equals(column.getKey())) {

            WikipediaSearchResultNode node = createNode((Node) column.getValue());

            if (nodeIds.get(node.getId()) == null) {

              nodeIds.put(node.getId(), nodeIds.size());
              result.addNode(node);
            }
          }
          else if ("r".equals(column.getKey())) {

            WikipediaSearchResultRelationship relationship = createRelationship((Relationship) column.getValue());

            if (relationship != null && relationshipIds.get(relationship.getId()) == null) {

              relationshipIds.put(relationship.getId(), relationshipIds.size());
              result.addRelationship(relationship);
            }
          }
        }
      }

      remapRelationships(nodeIds, result);
      
      if (autoComplete) {

        autoComplete(result);
      }

      tx.success();
    }

    return result;
  }

  /**
   * 
   * @param node
   * @return
   */
  private WikipediaSearchResultNode createNode(Node node) {

    WikipediaSearchResultNode result = new WikipediaSearchResultNode();

    result.setId(node.getId());
    result.setName((String) node.getProperty("title"));

    String thumbnail = null;

    try {
      thumbnail = (String) node.getProperty("url");
      thumbnail = (thumbnail != null) ? thumbnail : (String) node.getProperty("image");
    }
    catch (Exception e) {
    }

    result.setThumbnail(thumbnail);
    result.setType(node.getLabels().iterator().next().name());
    result.setGroup(1);

    return result;
  }

  /**
   * 
   * @param relationship
   * @return
   */
  private WikipediaSearchResultRelationship createRelationship(Relationship relationship) {

    WikipediaSearchResultRelationship result = new WikipediaSearchResultRelationship();

    result.setId(relationship.getId());
    result.setSource(relationship.getStartNode().getId());
    result.setTarget(relationship.getEndNode().getId());
    result.setLinkName(relationship.getType().name());

    return result;
  }
  
  /**
   * 
   * @param nodeIds
   * @param result
   */
  private void remapRelationships(Map<Long, Integer> nodeIds, WikipediaSearchResult result) {
    
    List<WikipediaSearchResultRelationship> relationships = result.getRelationships();
    
    for (int i = 0; i < relationships.size(); i++) {
      
      WikipediaSearchResultRelationship relationship = relationships.get(i);
      
      Integer source = nodeIds.get(relationship.getSource());
      Integer target = nodeIds.get(relationship.getTarget());

      if (source != null && target != null) {
        
        relationship.setSource(source);
        relationship.setTarget(target);
      }
      else {
        
        relationships.remove(i);
      }
    }
  }
  
  /**
   * @param result
   */
  private void autoComplete(WikipediaSearchResult result) {
  }
}
