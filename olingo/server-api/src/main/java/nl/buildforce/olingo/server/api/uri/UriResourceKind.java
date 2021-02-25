/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.uri;

/**
 * Defining the various resource part types
 */
public enum UriResourceKind {

  /**
   * Class: {@link UriResourceAction}<br>
   * URI: http://.../serviceroot/action()
   */
  action,

  /**
   * Class: {@link UriResourceComplexProperty}<br>
   * URI: http://.../serviceroot/entityset(1)/complexproperty()
   */
  complexProperty,

  /**
   * Class: {@link UriResourceCount}<br>
   * URI: http://.../serviceroot/entityset/$count
   */
  count,

  /**
   * Class: {@link UriResourceEntitySet}<br>
   * URI: http://.../serviceroot/entityset
   */
  entitySet,

  /**
   * Class: {@link UriResourceFunction}<br>
   * URI: http://.../serviceroot/functionimport(P1=1,P2='a')
   */
  function,

  /**
   * Class: {@link UriResourceIt}<br>
   * URI: http://.../serviceroot/entityset?$filter=$it/property
   */
  it,

  /**
   * Class: {@link UriResourceLambdaAll}<br>
   * URI: http://.../serviceroot/entityset/all(...)
   */
  lambdaAll,

  /**
   * Class: {@link UriResourceLambdaAny}<br>
   * URI: http://.../serviceroot/entityset/any(...)
   */
  lambdaAny,

  /**
   * Class: {@link UriResourceLambdaVariable}<br>
   * URI: http://.../serviceroot/entityset/listofstring/any(d: 'string' eq d)
   */
  lambdaVariable,

  /**
   * Class: {@link UriResourceNavigation}<br>
   * URI: http://.../serviceroot/entityset(1)/navProperty
   */
  navigationProperty,

  /**
   * Class: {@link UriResourceRef}<br>
   * URI: http://.../serviceroot/entityset/$ref
   */
  ref,

  /**
   * Class: {@link UriResourceRoot}<br>
   * URI: http://.../serviceroot/entityset(1)?$filter=property eq $root/singleton/configstring
   */
  root,

  /**
   * Class: {@link UriResourceProperty}<br>
   * URI: http://.../serviceroot/entityset(1)/property
   */
  primitiveProperty,

  /**
   * Class: {@link UriResourceSingleton}<br>
   * URI: http://.../serviceroot/singleton
   */
  singleton,

  /**
   * Class: {@link UriResourceValue}<br>
   * URI: http://.../serviceroot/entityset(1)/property/$value
   */
  value,
}
