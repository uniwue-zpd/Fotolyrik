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
    originalFilename: string | null;
    filename: string | null;
    path: string | null;
    type: string | null;
    size: number | null
}

export interface FileDTO {
    id: number;
    originalFilename: string;
    filename: string;
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

export interface GeneralSearchResult {
    id: number;
    title: string;
    type: string;
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

export interface PersonDTO {
    id: number;
    fullName: string | null;
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
    themes: Keyword[] | [];
    imageMotifs: Keyword[] | [];
    form: string | null;
    link: string | null;
    iiifManifest: string | null;
    images: File[] | []
    copyrightStatusImage: string | null;
    copyrightStatusText: string | null;
    languages: [string] | [];
}

export interface PhotoPoemDTO extends Auditable {
    title: string;
    subtitle: string | null;
    altTitle: string | null;
    volume: number | null;
    issue: number | null;
    pageNumber: number | null;
    pageCount: number | null;
    publicationDate: string | null;
    publicationMedium: PubMediumDTO | null;
    authors: PersonDTO[] | [];
    photographers: PersonDTO[] | [];
    otherContributors: PersonDTO[] | [];
    themes: KeywordDTO[] | [];
    imageMotifs: KeywordDTO[] | [];
    form: string | null;
    link: string | null;
    iiifManifest: string | null;
    images: FileDTO[] | []
    copyrightStatusImage: string | null;
    copyrightStatusText: string | null;
    languages: [string] | [];
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

export interface PubMediumDTO {
    id: number;
    title: number;
}

export interface Keyword extends Auditable {
    value: string;
    gndId: string | null;
}

export interface KeywordDTO {
    id: number;
    value: string;
}
