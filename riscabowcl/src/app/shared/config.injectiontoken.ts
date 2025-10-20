import { InjectionToken } from '@angular/core';
import { StepConfig } from './models';

export const CONFIG = new InjectionToken<StepConfig>('config-data');
