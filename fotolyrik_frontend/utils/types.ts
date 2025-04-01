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

export interface Person extends Auditable {
    firstName: string,
    lastName: string,
    pseudonym?: string,
    birthYear?: string,
    deathYear?: string,
    sex?: "MALE" | "FEMALE"
}

export interface PhotoPoem extends Auditable {
    title: string,
    volume?: string,
    issue?: string,
    pageNumber?: number,
    publicationDate?: string,
    publicationMedium?: string,
    author?: Person,
    photographer?: Person,
    link?: string,
    iiifManifest: string

    //TODO: Add other required fields
}
