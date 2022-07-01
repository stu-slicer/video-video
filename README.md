# Streaming in Java

## Streaming over HTML

HTML5 has video streaming, which allows a file to be streamed and viewed within a video window.

In HTML

```html
<video width="750" autoplay controls>
    <source src="http://localhost:8080/video/stream/mp4/glasto-self-esteem" type="video/mp4" >
</video>
```

A couple of things:

- **autoplay** - allows the video to start playing. Note that some browsers require the user to specifically allow this option on a site by site basis.
- **controls** - displays a set of controls for the view.
- **muted** - mutes the sound.

## Streaming

Music and video files are typically very large, in the range of 10s MB to 5 -15 GBs for films.

There is now way that these can be downloaded in a single go, the internet couldn’t cope - the bandwidth is just not there.

To solve this a file is downloaded in chunks of bytes, say 300K per chunk, starting at the beginning of the file and continuing until the end.

The browser controls the flow, asks for the each chunk of data as and when required using a simple GET request.

## HTTP Requests

### Request headers

The client requests the range of data required, with a from and an optional to.

Note the initial request is slightly different, it has no to - so allowing the server to respond with the chunk size to use.

```html
// initial request
**Range: bytes=0-**

```

Subsequent requests just pass the start of the chunk, the length is already known so implied.

```html
// a subsequent request
**Range: bytes=3776412-**
```

### Response headers

Accept ranges indicates the type of data (bytes)

```html
Accept-Ranges: bytes
```

Content range tells the client the range of data that is being returned.

```html
Content-Range: bytes 3776412-4091112/5095029385
```

Content type and content length are standard response headers and obviously reflect the type of data and the length.

```html
Content-Type: video/mp4
Content-Length: 314701
```

### HTTP Status

Whilst the file is being downloaded the response status is 206 - Partial Content, at end of file the status is a simple 200 - Okay.

## Spring and Streaming

The streaming is ‘basic’ file handling - opening files and reading the required bytes and returning as a HTTP response.

The only major complication is the use of Reactor project (another Spring project). This applies the concepts of react programming which improves the performance of an application by utilising as much as of the CPU as possible, by eliminating as much waiting time as possible.

### VideoStreamController

`VideoStreamController` is a simple `RestController` with a single GET method.

Extracts the Range from the HTTP readers to determine what chunk of the file to return.

### VideoStreamService

`VideoStreamService` is a bit more complicated, only in that it needs to determine the range to return and handle the suitable response headers and return codes.

### ByteReader

The task of reading the bytes from a file has been abstracted into an interface - `ByteReader`, allowing multiple implementations - for checking performance etc.

**Simple**

Very simple, the whole file is read into a byte array and the appropriate section copied to a return array. Not suitable for long files - uses too much memory for one.

**Random**

Uses the Java `RandomAccessFile`, which allows random reading of data from anywhere within the file - suing seek().

**CachedRandom**

A variation of the random reader, but caches open files between reads - removing a small overhead.

### application.properties

A couple of properties can be set; the file directory and which `ByteReader` config to use.

```
## directory path for films.
## Important - on Windows you must include drive and escape the backslash!
film.directory=D:\\_Development\\intellij\\workspace-nology\\video-video\\films

## type of ByteReader to use.
## - simple - very simple, reads whole of file into memory every time!
## - random - use a random access file
## - cachingRandom - use a random access file, cache opened file between accesses.
byte-reader.type=cachingRandom
```

### Examples HTTP Request

Initial GET

```
GET /video/stream/mp4/glasto-22-self-esteem HTTP/1.1
Host: localhost:8080
User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:102.0) Gecko/20100101 Firefox/102.0
Accept: video/webm,video/ogg,video/*;q=0.9,application/ogg;q=0.7,audio/*;q=0.6,*/*;q=0.5
Accept-Language: en-GB,en-US;q=0.7,en;q=0.3
Range: bytes=0-
Connection: keep-alive
Referer: http://127.0.0.1:5555/
Sec-Fetch-Dest: video
Sec-Fetch-Mode: no-cors
Sec-Fetch-Site: cross-site

HTTP/1.1 206 Partial Content
Content-Type: video/mp4
Accept-Ranges: bytes
Content-Range: bytes 0-314700/5095029385
Content-Length: 314701

```

A later GET (same file)

```
GET /video/stream/mp4/glasto-22-self-esteem HTTP/1.1
Host: localhost:8080
User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:102.0) Gecko/20100101 Firefox/102.0
Accept: video/webm,video/ogg,video/*;q=0.9,application/ogg;q=0.7,audio/*;q=0.6,*/*;q=0.5
Accept-Language: en-GB,en-US;q=0.7,en;q=0.3
Range: bytes=3776412-
Connection: keep-alive
Referer: http://127.0.0.1:5555/
Sec-Fetch-Dest: video
Sec-Fetch-Mode: no-cors
Sec-Fetch-Site: cross-site

HTTP/1.1 206 Partial Content
Content-Type: video/mp4
Accept-Ranges: bytes
Content-Range: bytes 3776412-4091112/5095029385
Content-Length: 314701
```

### Credit

The streaming application was based on a Medium article by **Saravanakumar Arunachalam.**

[Video Streaming over HTTP using Spring Boot](https://saravanastar.medium.com/video-streaming-over-http-using-spring-boot-51e9830a3b8)