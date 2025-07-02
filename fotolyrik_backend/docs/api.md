# API documentation

## Endpoints

### `/persons`
- **Method**: `GET`
    - **Description**: Fetch a list of persons.
    - Response: `200 OK`
- **Method**: `POST`
    - Description: Create a new person.
    - Request Body: JSON object with person details.
    - Response: `201 Created`

#### `/persons/{id}`
- **Method**: `GET`
    - Description: Fetch a person by ID.
    - Response: `200 OK`
    - Response on error: `404 Not Found`
- **Method**: `PUT`
    - Description: Update a person by ID.
    - Request Body: JSON object with updated person details.
    - Response: `201 Updated`
    - Response on error: `404 Not Found`
- **Method**: `DELETE`
    - Description: Delete a person by ID.
    - Response: `204 No Content`
    - Response on error: `404 Not Found`

---

### `/photopoems`
- **Method**: `GET`
    - Description: Fetch a list of photopoems.
    - Response: `200 OK`
- **Method**: `POST`
    - Description: Create a new photopoem.
    - Request Body: JSON object with photopoem details.
    - Response: `201 Created`

#### `/photopoems/{id}`
- **Method**: `GET`
    - Description: Fetch a photopoem by ID.
    - Response: `200 OK`
- **Method**: `PUT`
    - Description: Update a photopoem by ID.
    - Request Body: JSON object with updated photopoem details.
    - Response: `201 Updated`
    - Response on error: `404 Not Found`
- **Method**: `DELETE`
    - Description: Delete a photopoem by ID.
    - Response: `204 No Content`
    - Response on error: `404 Not Found`

---

### `/publication_media`
- **Method**: `GET`
    - Description: Fetch a list of publication media.
    - Response: `200 OK`
- **Method**: `POST`
    - Description: Create a new publication media.
    - Request Body: JSON object with publication media details.
    - Response: `201 Created`

#### `/publication_media/{id}`
- **Method**: `GET`
    - Description: Fetch a publication media by ID.
    - Response: `200 OK`
    - Response on error: `404 Not Found`
- **Method**: `PUT`
    - Description: Update a publication media by ID.
    - Request Body: JSON object with updated publication media details.
    - Response: `201 Updated`
    - Response on error: `404 Not Found`
- **Method**: `DELETE`
    - Description: Delete a publication media by ID.
    - Response: `204 No Content`
    - Response on error: `404 Not Found`

---

### `/places`
- **Method**: `GET`
    - Description: Fetch a list of places.
    - Response: `200 OK`
- **Method**: `POST`
    - Description: Create a new place.
    - Request Body: JSON object with place details.
    - Response: `201 Created`

#### `/places/{id}`
- **Method**: `GET`
    - Description: Fetch a place by ID.
    - Response: `200 OK`
    - Response on error: `404 Not Found`
- **Method**: `PUT`
    - Description: Update a place by ID.
    - Request Body: JSON object with updated place details.
    - Response: `201 Updated`
    - Response on error: `404 Not Found`
- **Method**: `DELETE`
    - Description: Delete a place by ID.
    - Response: `204 No Content`
    - Response on error: `404 Not Found`

---

### `/contact`
- **Method**: `POST`
  - Description: Send a contact message.
  - Request Body: JSON object with contact details.
  - Response: `200 OK`

---

### `/files`
- **Method**: `GET`
  - Description: Fetch a list of files.
  - Response: `200 OK`
- **Method**: `POST`
  - Description: Upload new file.
  - Request Body: `multipart/form-data` with `file` parameter.
  - Response: `201 Created`

#### `/files/{id}`
- **Method**: `GET`
  - Description: Fetch a file by ID.
  - Response: `200 OK`
  - Response on error: `404 Not Found`
- **Method**: `DELETE`
  - Description: Delete a file by ID.
  - Response: `204 No Content`
  - Response on error: `404 Not Found`
