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

export interface CopyrightStatus extends Auditable {
    value: string;
    description: string | null;
}

export interface CopyrightStatusDTO {
    id: number;
    value: string;
    description: string | null;
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

export interface Language extends Auditable {
    name: string;
    isoDesignation: string;
}

export interface LanguageDTO {
    id: number;
    name: string;
    isoDesignation: string;
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
    subtitle: string | null;
    altTitle: string | null;
    volume: string | null;
    issue: string | null;
    pageNumber: number | null;
    manifestPageNumber: number | null;
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
    copyrightStatusImage: CopyrightStatus | null;
    copyrightStatusText: CopyrightStatus | null;
    languages: Language[] | [];
}

export interface PhotoPoemDTO extends Auditable {
    title: string;
    subtitle: string | null;
    altTitle: string | null;
    volume: number | null;
    issue: number | null;
    pageNumber: number | null;
    manifestPageNumber: number | null;
    pageCount: number | null;
    publicationDate: string | null;
    publicationMedium: PubMediumPreviewDTO | null;
    authors: PersonDTO[] | [];
    photographers: PersonDTO[] | [];
    otherContributors: PersonDTO[] | [];
    themes: KeywordDTO[] | [];
    imageMotifs: KeywordDTO[] | [];
    form: string | null;
    link: string | null;
    iiifManifest: string | null;
    images: FileDTO[] | []
    copyrightStatusImage: CopyrightStatusDTO | null;
    copyrightStatusText: CopyrightStatusDTO | null;
    languages: LanguageDTO[] | [];
}

export interface Place extends Auditable {
    name: string;
    description: string | null;
    latitude: number | null;
    longitude: number | null
}

export interface PlaceDTO {
    id: number;
    name: string | null;
}

export interface PubMedium extends Auditable {
    title: string;
    subtitle: string | null;
    publicationPlaces: Place[] | [];
    publisher: Publisher | null;
    pubRhythms: PubRhythm[] | [];
    editorialOffice: string | null;
    startYear: string | null;
    endYear: string | null;
    amountVolumes: number | null;
    amountIssues: number | null;
    zdbId: string | null;
    notes: string | null;
}

export interface PubMediumPreviewDTO {
    id: number;
    title: number;
}

export interface PubMediumDTO extends Auditable {
    title: string;
    subtitle: string | null;
    publicationPlaces: PlaceDTO[] | [];
    publisher: PublisherDTO | null;
    pubRhythms: PubRhythmDTO[] | [];
    editorialOffice: string | null;
    startYear: string | null;
    endYear: string | null;
    amountVolumes: number | null;
    amountIssues: number | null;
    zdbId: string | null;
    notes: string | null;
}

export interface Publisher extends Auditable {
    name: string | null;
    description: string | null;
}

export interface PublisherDTO {
    id: number;
    name: string | null;
}

export interface PubRhythm extends Auditable {
    value: string | null;
    description: string | null;
}

export interface PubRhythmDTO {
    id: number;
    value: string | null;
}

export interface Keyword extends Auditable {
    value: string;
    gndId: string | null;
}

export interface KeywordDTO {
    id: number;
    value: string;
}
