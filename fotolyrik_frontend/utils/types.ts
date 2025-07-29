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
    fullText: string | null;
}

export interface FullTextSearchResult {
    photopoemId: number;
    photopoemTitle: string;
    queryResult: string;
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
    publicationPlaces: Place[] | [];
    publisher: string | null;
    pubRhytm: string | null;
    startYear: string | null;
    endYear: string | null;
    amountVolumes: number | null;
    amountIssues: number | null;
    zdbId: string | null;
}
