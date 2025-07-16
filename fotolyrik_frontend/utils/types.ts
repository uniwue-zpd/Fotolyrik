export interface Auditable {
    id: number;
    created_date: string;
    created_by: string;
    last_modified_date: string;
    last_modified_by: string;
}

export interface ContactForm {
    name: string | null,
    appellation: string | null,
    email: string | null,
    subject: string | null,
    message: string | null
}

export interface File extends Auditable {
    filename: string | null;
    path: string | null;
    type: string | null;
    size: number | null
}

export interface FullText extends Auditable {
    photopoem: PhotoPoem;
    full_text: string | null;
}

export interface FullTextSearchResult {
    photopoem_id: number;
    photopoem_title: string;
    query_result: string;
}

export interface Person extends Auditable {
    first_name: string | null;
    last_name: string | null;
    full_name: string | null;
    pseudonyms: [string] | [];
    birth_year: number | null;
    death_year: number | null;
    sex: "weiblich" | "männlich" | null;
    gnd_id: string | null;
}

export interface PhotoPoem extends Auditable {
    title: string;
    volume: string | null;
    issue: string | null;
    page_number: number | null;
    page_count: number | null;
    publication_date: string | null;
    publication_medium: PubMedium | null;
    authors: Person[] | [];
    photographers: Person[] | [];
    other_contributors: Person[] | [];
    themes: [string] | [];
    topics: [string] | [];
    form: string | null;
    link: string | null;
    iiif_manifest: string | null;
    images: File[] | []
    copyright_status_image: string | null;
    copyright_status_text: string | null;
    language: string | null;
}

export interface Place extends Auditable {
    name: string;
    description: string | null;
    latitude: number | null;
    longitude: number | null
}

export interface PubMedium extends Auditable {
    title: string;
    subtitle: string | null;
    publication_places: Place[] | [];
    publisher: string | null;
    pub_rhytm: string | null;
    start_year: string | null;
    end_year: string | null;
    amount_volumes: number | null;
    amount_issues: number | null;
    zdb_id: string | null;
}
