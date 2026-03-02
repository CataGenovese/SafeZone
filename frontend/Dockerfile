# simple nginx image serving static frontend files
FROM nginx:alpine

# copy all frontend files (e.g. your built assets) into nginx html directory
COPY . /usr/share/nginx/html

# container listens on port 80 by default
EXPOSE 80
