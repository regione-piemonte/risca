import {
  RouteReuseStrategy,
  DetachedRouteHandle,
  ActivatedRouteSnapshot,
} from '@angular/router';

interface IRouteConfigData {
  reuse: boolean;
}

interface ICachedRoute {
  handle: DetachedRouteHandle;
  data: IRouteConfigData;
}

export class RiscaReuseStrategy implements RouteReuseStrategy {
  private routeCache = new Map<string, ICachedRoute>();

  /*
   * nome metodo "shouldReuseRoute"; descrizione:
   * @param (future: ActivatedRouteSnapshot, curr: ActivatedRouteSnapshot)
   * @returns boolean
   */

  shouldReuseRoute(
    future: ActivatedRouteSnapshot,
    curr: ActivatedRouteSnapshot
  ): boolean {
    let ret = future.routeConfig === curr.routeConfig;
    if (ret) {
      this.addRedirectsRecursively(future); // update redirects
    }

    return ret;
  }

  /*
   * nome metodo "shouldDetach"; descrizione:
   * @param (route: ActivatedRouteSnapshot)
   * @returns boolean
   */

  shouldDetach(route: ActivatedRouteSnapshot): boolean {
    const data = this.getRouteData(route);
    return data && data.reuse;
  }

  /*
   * nome metodo "store"; descrizione:
   * @param (route: ActivatedRouteSnapshot, handle: DetachedRouteHandle)
   * @returns void
   */

  store(route: ActivatedRouteSnapshot, handle: DetachedRouteHandle): void {
    const url = this.getFullRouteUrl(route);
    const data = this.getRouteData(route);
    this.routeCache.set(url, { handle, data });
    this.addRedirectsRecursively(route);
  }

  /*
   * nome metodo "shouldAttach"; descrizione:
   * @param (route: ActivatedRouteSnapshot)
   * @returns boolean
   */

  shouldAttach(route: ActivatedRouteSnapshot): boolean {
    const url = this.getFullRouteUrl(route);
    return this.routeCache.has(url);
  }

  /*
   * nome metodo "retrieve"; descrizione:
   * @param (route: ActivatedRouteSnapshot)
   * @returns DetachedRouteHandle
   */

  retrieve(route: ActivatedRouteSnapshot): DetachedRouteHandle {
    const url = this.getFullRouteUrl(route);
    const data = this.getRouteData(route);
    return data && data.reuse && this.routeCache.has(url)
      ? this.routeCache.get(url).handle
      : null;
  }

  private addRedirectsRecursively(route: ActivatedRouteSnapshot): void {
    const config = route.routeConfig;
    if (config) {
      if (!config.loadChildren) {
        const routeFirstChild = route.firstChild;
        const routeFirstChildUrl = routeFirstChild
          ? this.getRouteUrlPaths(routeFirstChild).join('/')
          : '';
        const childConfigs = config.children;
        if (childConfigs) {
          const childConfigWithRedirect = childConfigs.find(
            (c) => c.path === '' && !!c.redirectTo
          );
          if (childConfigWithRedirect) {
            childConfigWithRedirect.redirectTo = routeFirstChildUrl;
          }
        }
      }
      route.children.forEach((childRoute) =>
        this.addRedirectsRecursively(childRoute)
      );
    }
  }

  private getFullRouteUrl(route: ActivatedRouteSnapshot): string {
    return this.getFullRouteUrlPaths(route).filter(Boolean).join('/');
  }

  private getFullRouteUrlPaths(route: ActivatedRouteSnapshot): string[] {
    const paths = this.getRouteUrlPaths(route);
    return route.parent
      ? [...this.getFullRouteUrlPaths(route.parent), ...paths]
      : paths;
  }

  private getRouteUrlPaths(route: ActivatedRouteSnapshot): string[] {
    return route.url.map((urlSegment) => urlSegment.path);
  }

  private getRouteData(route: ActivatedRouteSnapshot): IRouteConfigData {
    return route.routeConfig && (route.routeConfig.data as IRouteConfigData);
  }
}
