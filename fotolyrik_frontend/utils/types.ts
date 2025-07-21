export interface Auditable {
    id: number;
    createdDate: string;
    createdBy: string;
    lastModifiedDate: string;
    lastModifiedBy: string;
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
    firstName: string | null;
    lastName: string | null;
    fullName: string | null;
    pseudonyms: [string] | [];
    birthYear: number | null;
    deathYear: number | null;
    sex: "weiblich" | "männlich" | null;
    gndId: string | null;
    image: File | null;
}

export interface PhotoPoem extends Auditable {
    title: string;
    volume: string | null;
    issue: string | null;
    pageNumber: number | null;
    pageCount: number | null;
    publicationDate: string | null;
    publicationMedium: PubMedium | null;
    authors: Person[] | [];
    photographers: Person[] | [];
    otherContributors: Person[] | [];
    themes: [string] | [];
    topics: [string] | [];
    form: string | null;
    link: string | null;
    iiifManifest: string | null;
    images: File[] | []
    copyrightStatusImage: string | null;
    copyrightStatusText: string | null;
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
