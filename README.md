# Rubrica API

Un servizio che permette di gestire la propria rubrica attraverso richieste HTTP. Tutte le funzioni del servizio restituiscono un JSON contenente i seguenti campi:

- `status` ("ok" o "error")
- `result` (se lo status è "ok")
- `message` (se lo status è "error")

## Funzioni del Servizio

### 1. `createToken`

Permette di registrarsi al servizio con un username e una password.

#### Parametri GET:

- `username` (string)
- `password` (string)

#### Esempio:

localhost:8080/createToken?username=a&password=a

markdown
Copy code

### 2. `getToken`

Permette di recuperare il token da utilizzare per le successive richieste.

#### Parametri GET:

- `username` (string)
- `password` (string)

#### Esempio:

localhost:8080/getToken?username=a&password=a

markdown
Copy code

### 3. `addContact`

Permette di aggiungere un contatto alla rubrica.

#### Parametri GET:

- `token` (string)
- `nome` (string)
- `cognome` (string)
- `numero` (string)
- `gruppo` (string - opzionale)

#### Esempio:

localhost:8080/addContact?token=3b2a24f945d346a59b1ed950f20a7723&nome=aaa&cognome=bbb&numero=123456&gruppo=abc

markdown
Copy code

### 4. `deleteContact`

Permette di cancellare un contatto dalla rubrica.

#### Parametri GET:

- `token` (string)
- `id` (int)

#### Esempio:

localhost:8080/deleteContact?token=3b2a24f945d346a59b1ed950f20a7723&id=50

shell
Copy code

### 5. `getGroups`

Restituisce la lista di tutti i gruppi presenti nella rubrica.

#### Parametri GET:

- `token` (string)

#### Esempio:

localhost:8080/getGroups?token=3b2a24f945d346a59b1ed950f20a7723

shell
Copy code

### 6. `getContacts`

Restituisce la lista di tutti i contatti presenti nella rubrica.

#### Parametri GET:

- `token` (string)

#### Esempio:

localhost:8080/getContacts?token=3b2a24f945d346a59b1ed950f20a7723

markdown
Copy code

## Test

Esempio di utilizzo delle funzioni attraverso richieste HTTP.

1. **Registrazione utente**:
localhost:8080/createToken?username=a&password=a

markdown
Copy code

2. **Recupero del token**:
localhost:8080/getToken?username=a&password=a

markdown
Copy code

3. **Aggiunta di un contatto alla rubrica**:
localhost:8080/addContact?token=3b2a24f945d346a59b1ed950f20a7723&nome=aaa&cognome=bbb&numero=123456&gruppo=abc

markdown
Copy code

4. **Cancellazione di un contatto dalla rubrica**:
localhost:8080/deleteContact?token=3b2a24f945d346a59b1ed950f20a7723&id=50

markdown
Copy code

5. **Ottenere la lista di gruppi nella rubrica**:
localhost:8080/getGroups?token=3b2a24f945d346a59b1ed950f20a7723

markdown
Copy code

6. **Ottenere la lista di contatti nella rubrica**:
localhost:8080/getContacts?token=3b2a24f945d346a59b1ed950f20a7723

less
Copy code

Ricorda di sostituire i valori dei parametri (`token`, `id`, etc.) con quelli reali ottenuti dalle richieste precedenti.
go
Copy code

Puoi copiare e incollare questo codice direttamente in un file `README.md` nel tuo repository.