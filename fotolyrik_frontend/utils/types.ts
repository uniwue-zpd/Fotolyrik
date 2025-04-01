export interface Auditable {
    id: number,
    created_date: string,
    created_by: string,
    last_modified_date: string,
    last_modified_by: string
}

export interface Place extends Auditable {
    name: string,
    description: string,
    latitude: number,
    longitude: number
}

export interface Person extends Auditable {
    first_name: string,
    last_name: string,
    pseudonym?: string,
    birth_year?: string,
    death_year?: string,
    sex?: "MALE" | "FEMALE"
}

export interface PhotoPoem extends Auditable {
    title: string,
    volume?: string,
    issue?: string,
    page_number?: number,
    publication_date?: string,
    publication_medium?: string,
    author?: Person,
    photographer?: Person,
    link?: string,
    iiif_manifest: string

    //TODO: Add other required fields
}

export interface PubMedium extends Auditable {
    title: string,
    subtitle?: string,
    publication_places?: Place[],
    publisher?: string,
    pub_rhytm?: "W" | "M" | "HM",
    start_year?: string,
    end_year?: string,
    amount_volumes?: number,
    amount_issues?: number,
}
