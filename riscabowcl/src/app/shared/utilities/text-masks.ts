import createNumberMask from 'text-mask-addons/dist/createNumberMask';
import createAutoCorrectedDatePipe from 'text-mask-addons/dist/createAutoCorrectedDatePipe'

///////////////////////////////////////
/// MASKS
///////////////////////////////////////
export const COD_PROVINCIA_MASK =   [/[A-Z]/i, /[A-Z]/i];

export const ITALIAN_DATE_MASK = [
    /\d/, /\d/, '/', /\d/, /\d/, '/', /\d/, /\d/, /\d/, /\d/
];

export const IBAN_TEXTMASK =
['I', 'T', /\d/, /\d/, '-',
/[A-Z]/i, /\d/, /\d/, /\d/, '-',
/\d/, /\d/, /\d/, /\d/, '-',
/\d/, /\d/, /\d/, /\d/, '-',
/\d/, /\d/, /\d/, /\d/, '-',
/\d/, /\d/, /\d/, /\d/, '-',
/\d/, /\d/, /\d/
];

export const EURO_VALUE_MASK = createNumberMask({
    allowDecimal: true,
    prefix: '€',
    suffix: '',
    requireDecimal: true,
    decimalSymbol: ',',
    thousandsSeparatorSymbol: '.'
});

export const CF_MASK = 
[/[A-Z]/i,/[A-Z]/i,/[A-Z]/i,/[A-Z]/i,/[A-Z]/i,/[A-Z]/i,
 /\d/,/\d/,/[A-Z]/i,/\d/,/\d/,
 /[A-Z]/i,/\d/,/\d/,/\d/,/[A-Z]/i 
];

export const CAP_MASK = [/[0-9]/,/[0-9]/,/[0-9]/,/[0-9]/,/[0-9]/];

export const ITALIAN_TELEPHONE_MASK = [
    '+39-',/\d/,/\d/,/\d/,/\d/,/\d/,/\d/,/\d/,/\d/,/\d/,/\d/,/\d/,/\d/
];

export function createDenominationMask(length: number, numbersAllowed: boolean = false) {
    const item = (!numbersAllowed ? /([A-Z]|\s|')/i : /([A-Z]|[0-9]|\s|')/i);
    const pattern: Array<any> = new Array<any>(length);
    for (let i = 0; i < pattern.length; i++) {
        pattern[i] = item;
    }
    return pattern;
}

////////////////////////////////
/// PIPES
////////////////////////////////
export const ITALIAN_DATE_PIPE = createAutoCorrectedDatePipe('dd/mm/yyyy');

export const TO_UPPER_PIPE = function(conformedValue: string) {
    if (conformedValue != null){
      return conformedValue.toUpperCase();
    }
    else return conformedValue;
}

export const TO_LOWER_PIPE = function(conformedValue: string) {
    if (conformedValue != null){
      return conformedValue.toLowerCase();
    }
    else return conformedValue;
}

export const ITALIAN_DATE_FORMATTER = function(dateString: string): string{
    if (dateString){
        var date = new Date(dateString);
        var formattedDate = date.getDate()+"/"+(date.getMonth()<10 ? '0'+date.getMonth() : date.getMonth()) +"/"+date.getFullYear();
        return formattedDate;
    }
    else{
        return null;
    }
}