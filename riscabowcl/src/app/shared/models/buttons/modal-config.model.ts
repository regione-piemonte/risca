interface ModalButton {
    label?: string;
    type?: string;
    callback?: Function;
}

export interface ModalConfig {
    title: string;
    codMess: string;
    buttons: ModalButton[];
}
