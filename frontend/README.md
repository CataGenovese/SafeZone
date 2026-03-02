# DemoBank Frontend

This is a simple Next.js web application implementing the requested DemoBank features.

## Features

- Login page
- Dashboard with links for transfer, change password, and payment
- Each action sends a POST to `/api/fraud` with relevant data and displays risk score

## Running Locally

1. `cd frontend`
2. `npm install`
3. `npm run dev`
4. Open http://localhost:3000/login

## Docker

You can build a container for the frontend and run it with Docker:

```bash
# from project root
docker-compose build
docker-compose up
```

`docker-compose.yml` defines both the backend and the frontend services; the backend image will be built from `backend/Dockerfile` and the frontend from `frontend/Dockerfile`. The fraud endpoint is served by Next.js itself under `/api/fraud`.

## Notes

- The fraud API returns `{ action: 'ok'|'otp'|'block', score: number }`.
- All logic is mocked for demo purposes. Replace `/api/fraud` with your real fraud API as needed.
