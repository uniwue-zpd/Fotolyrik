export interface Auditable {
    id: number,
    createdDate: string,
    createdBy: string,
    lastModifiedDate: string,
    lastModifiedBy: string
}

export interface Place extends Auditable {
    name: string,
    description: string,
    latitude: number,
    longitude: number
}
