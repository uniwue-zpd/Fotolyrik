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


export interface CopyrightStatusDTO extends Auditable {
    id: number;
    value: string;
    description: string | null;
}


export interface FileDTO extends Auditable {
    originalFilename: string;
    filename: string;
}

export interface FullTextDTO extends Auditable {
    photopoem: PhotoPoemPreviewDTO;
    fullText: string;
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

export interface LanguageDTO extends Auditable {
    name: string;
    isoDesignation: string;
}

export interface PersonDTO extends Auditable {
    firstName: string | null;
    lastName: string | null;
    studioName: string | null;
    fullName: string | null;
    pseudonyms: [string] | [];
    birthYear: number | null;
    deathYear: number | null;
    sex: "weiblich" | "männlich" | null;
    gndId: string | null;
    image: FileDTO | null;
}

export interface PersonPreviewDTO {
    id: number;
    fullName: string | null;
    studioName: string | null;
    pseudonyms: [string] | [];
}
export enum ContributionRole{
    AUTHOR = "AUTHOR",
    PHOTOGRAPHER = "PHOTOGRAPHER",
    OTHER = "OTHER",
}
export interface ContributionDTO {
    id: number;
    role: ContributionRole;
    contributor: PersonPreviewDTO;
    pseudonym: string;
}

export interface PhotoPoemDTO extends Auditable {
    title: string | null;
    subtitle: string | null;
    altTitle: string | null;
    volume: number | null;
    issue: number | null;
    pageNumber: string | null;
    manifestPageNumber: number | null;
    pageCount: number | null;
    pictureCount: string | null;
    publicationDate: string | null;
    publicationMedium: PubMediumPreviewDTO | null;
    foundIn: LocationDTO[] | [];
    authors: PersonPreviewDTO[] | [];
    photographers: PersonPreviewDTO[] | [];
    depictedPeople: PersonPreviewDTO[] | [];
    otherContributors: PersonPreviewDTO[] | [];
    contributions: ContributionDTO[] | [];
    themes: KeywordPreviewDTO[] | [];
    imageMotifs: KeywordPreviewDTO[] | [];
    form: string | null;
    link: string | null;
    iiifManifest: string | null;
    images: FileDTO[] | [];
    imagesVisible: AccessLevel;
    copyrightStatusImage: CopyrightStatusDTO | null;
    copyrightStatusText: CopyrightStatusDTO | null;
    languages: LanguageDTO[] | [];
}

export interface PhotoPoemPreviewDTO {
    id: number;
    title: string | null;
    altTitle: string | null;
}

export interface PlaceDTO extends Auditable {
    name: string;
    description: string | null;
    latitude: number | null;
    longitude: number | null
}


export interface PubMedium extends Auditable {
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

export interface PubMediumPreviewDTO {
    id: number;
    title: number;
}
export interface LocationDTO extends Auditable {
    name: string;
    description: string;
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

export interface PublisherDTO extends Auditable {
    name: string | null;
    description: string | null;
}


export interface PubRhythmDTO extends Auditable {
    value: string | null;
    description: string | null;
}


export interface KeywordDTO extends Auditable {
    value: string;
    gndId: string | null;
}
export interface KeywordPreviewDTO {
    id: number;
    value: string;
}

/**
 * Defines the access levels
 * - PUBLIC: Visible to everyone.
 * - INTERNAL: Only visible to authenticated users.
 * - RESTRICTED: Only visible to users with specific permissions.
 */
export enum AccessLevel {
    PUBLIC = "PUBLIC",
    INTERNAL = "INTERNAL",
    RESTRICTED = "RESTRICTED"
}
