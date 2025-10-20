// This file can be replaced during build by using the `fileReplacements` array.
// `ng build ---prod` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

export const environment = {
  production: false,
  // beServerPrefix: 'https://dev-scriva.servizi.regione.piemonte.it',
  // beServerPrefix: 'http://dev-scrivasrv.csi.it',
  beServerPrefix: 'http://localhost:18000', // local server
  application: 'riscaboweb',
  mockServer: 'http://localhost:3000',
  apiGWServerPrefix: 'https://api-piemonte.ecosis.csi.it:443/api',
  apiAccessToken: '57f8c486-9390-3d5e-82f5-ebeeb6e729ec',
  localLogout: 'https://www.google.com/',
  jobPollingInterval: 10000,
};

/*
 * In development mode, to ignore zone related error stack frames such as
 * `zone.run`, `zoneDelegate.invokeTask` for easier debugging, you can
 * import the following file, but please comment it out in production mode
 * because it will have performance impact when throw error
 */
// import 'zone.js/dist/zone-error';  // Included with Angular CLI.
