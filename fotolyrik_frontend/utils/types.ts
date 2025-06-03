export interface Auditable {
    id: number,
    created_date: string,
    created_by: string,
    last_modified_date: string,
    last_modified_by: string
}

export interface Place extends Auditable {
    name: string,
    description: string | null,
    latitude: number | null,
    longitude: number | null
}

export interface Person extends Auditable {
    first_name: string | null,
    last_name: string | null,
    full_name: string | null,
    pseudonym: string | null,
    birth_year: number | null,
    death_year: number | null,
    sex: "MALE" | "FEMALE" | null
}

export interface PhotoPoem extends Auditable {
    title: string,
    volume: string | null,
    issue: string | null,
    page_number: number | null,
    publication_date: string | null,
    publication_medium: PubMedium | null,
    author: Person | null,
    photographer: Person | null,
    link: string | null,
    iiif_manifest: string | null,
    images: File[] | []
}

export interface PubMedium extends Auditable {
    title: string,
    subtitle: string | null,
    publication_places: Place[] | [],
    publisher: string | null,
    pub_rhytm: "M" | "HM" | "W" | "HW" | "DIVERS" | null,
    start_year: string | null,
    end_year: string | null,
    amount_volumes: number | null,
    amount_issues: number | null,
}

export interface File extends Auditable {
    filename: string | null,
    path: string | null,
    type: string | null,
    size: number | null
}
