export interface Auditable {
    id: number;
    createdDate: string;
    createdBy: string;
    lastModifiedDate: string;
    lastModifiedBy: string;
    internalNotes: string | null;
    generalNotes: string | null;
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
export interface CopyrightStatusPreviewDTO {
    id: number;
    value: string;
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
export interface LanguagePreviewDTO {
    id: number;
    name: string;
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
export enum ContributionRole {
    AUTHOR = "AUTHOR",
    PHOTOGRAPHER = "PHOTOGRAPHER",
    PARTICIPANT = "PARTICIPANT",
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
    series: string | null;
    volume: number | null;
    issue: number | null;
    pageNumber: string | null;
    manifestPageNumber: number | null;
    pageCount: number | null;
    pictureCount: string | null;
    publicationDate: string | null;
    publicationMedium: PubMediumPreviewDTO | null;
    foundIn: LocationPreviewDTO[] | [];
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
    copyrightStatusImage: CopyrightStatusPreviewDTO | null;
    copyrightStatusText: CopyrightStatusPreviewDTO | null;
    languages: LanguagePreviewDTO[] | [];
}

export interface PhotoPoemPreviewDTO {
    id: number;
    title: string | null;
    altTitle: string | null;
}
enum DepictedRole {
    DEPICTED = "DEPICTED"
}
export type PersonRole  = ContributionRole | DepictedRole;

export interface PhotoPoemPublicationDateDTO{
    id: number;
    title: string | null;
    publicationDate: string | null;
    role?: PersonRole;
}

export interface PlaceDTO extends Auditable {
    name: string;
    description: string | null;
    latitude: number | null;
    longitude: number | null
}

export interface PlacePreviewDTO {
    id: number;
    name: string;
}

export interface PubMediumPreviewDTO {
    id: number;
    title: number;
}
export interface LocationDTO extends Auditable {
    name: string;
    description: string;
}

export interface LocationPreviewDTO {
    id: number;
    name: string;
}

export interface PubMediumDTO extends Auditable {
    title: string;
    subtitle: string | null;
    publicationPlaces: PlacePreviewDTO[] | [];
    publisher: PublisherPreviewDTO | null;
    pubRhythms: PubRhythmPreviewDTO[] | [];
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

export interface PublisherPreviewDTO {
    id: number;
    name: string | null;
}

export interface PubRhythmDTO extends Auditable {
    value: string | null;
    description: string | null;
}

export interface PubRhythmPreviewDTO {
    id: number
    value: string | null;
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

// Visualizations area

/**
 * Describes a single datapoint used for visualizations, containing a keyword, its GND ID, and the count of occurrences.
 */
export interface KeywordCountDTO {
    keyword: string;
    gndId: string;
    id: number;
    count: number;
}

export interface PersonMetricsDTO {
    photopoems: number;
    pubMedia: number;
    keywords: number;
    authorsWorkedWith: number;
    photographersWorkedWith: number;
}

export interface PubMediumMetricsDTO {
    photopoems: number;
    keywords: number;
    authors: number;
    photographers: number;
    depictedPeople: number;
}

export interface PlaceMetricsDTO {
    pubMedia: number;
    photopoems: number;
    keywords: number;
    authors: number;
    photographers: number;
    depictedPeople: number;
}

/**
 * Describes a `Page` object returned after calling an endpoint with `Pageable` parameters
 */
export interface Page<T> {
    content: T[];
    pageable: {
        pageNumber: number;
        pageSize: number;
        sort: {
            empty: boolean;
            unsorted: boolean;
            sorted: boolean;
        };
        offset: number;
        unpaged: boolean;
        pages: boolean;
    };
    last: boolean;
    totalPages: number;
    totalElements: number;
    size: number;
    number: number;
    sort: {
        empty: boolean;
        unsorted: boolean;
        sorted: boolean;
    };
    numberOfElements: number;
    first: boolean;
    empty: boolean;
}

/**
 * Describes pagination and sorting parameters used for paged API requests.
 * Compatible with Spring Data Pageable query parameters:
 * - `page`: Zero-based page index to retrieve.
 * - `size`: Maximum number of elements per page.
 * - `sort`: Sorting criteria in the format `property,direction`
 *   (e.g. `title,asc` or `publicationDate,desc`). Multiple sort criteria
 *   can be provided as an array.
 */
export interface Pageable {
    page?: number;
    size?: number;
    sort?: string | string[];
}