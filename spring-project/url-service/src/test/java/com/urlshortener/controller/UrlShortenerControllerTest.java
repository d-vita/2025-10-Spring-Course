package com.urlshortener.controller;

class UrlShortenerControllerTest {
//
//    private UrlShortenerController controller;
//    private UrlShortener mockService;
//
//    @BeforeEach
//    void setUp() {
//        mockService = Mockito.mock(UrlShortener.class);
//        controller = new UrlShortenerController(mockService);
//    }
//
//    @Test
//    void testShortenUrl() {
//        String longUrl = "https://example.com";
//        String shortUrl = "http://short.ly/abc123";
//
//        Mockito.when(mockService.shorten(longUrl)).thenReturn(shortUrl);
//
//        ResponseEntity<ResponseDto> response = controller.shortenUrl(longUrl);
//
//        assertEquals(HttpStatus.CREATED, response.getStatusCode());
//        assertNotNull(response.getBody());
//        assertEquals(shortUrl, response.getBody().getUrl());
//    }

//    @Test
//    void testRetrieveUrl() {
//        String shortUrl = "http://short.ly/abc123";
//        String longUrl = "https://example.com";
//
//        Mockito.when(mockService.retrieve(shortUrl)).thenReturn(Optional.of(longUrl));
//
//        ResponseEntity<?> response = controller.retrieveUrl(shortUrl);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertNotNull(response.getBody());
//
//        ResponseDto body = (ResponseDto) response.getBody();
//        assertEquals(longUrl, body.getUrl());
//    }

//    @Test
//    void testRetrieveUrlNotFound() {
//        String shortUrl = "http://short.ly/unknown";
//
//        Mockito.when(mockService.retrieve(shortUrl)).thenReturn(Optional.empty());
//
//        ResponseEntity<?> response = controller.retrieveUrl(shortUrl);
//
//        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//        assertNotNull(response.getBody());
//        assertTrue(response.getBody() instanceof ErrorResponseDto);
//
//        ErrorResponseDto body = (ErrorResponseDto) response.getBody();
//        assertEquals("Short URL not found", body.getErrorMessage());
//    }
//
//    @Test
//    void testShortenUrlInvalidValues() {
//        Mockito.when(mockService.shorten(Mockito.isNull()))
//                .thenThrow(new UrlValidationException("URL is null"));
//        Mockito.when(mockService.shorten(""))
//                .thenThrow(new UrlValidationException("URL is empty"));
//        Mockito.when(mockService.shorten("   "))
//                .thenThrow(new UrlValidationException("URL is blank"));
//
//        assertThrows(UrlValidationException.class, () -> controller.shortenUrl(null));
//        assertThrows(UrlValidationException.class, () -> controller.shortenUrl(""));
//        assertThrows(UrlValidationException.class, () -> controller.shortenUrl("   "));
//    }
//
//    @Test
//    void testRetrieveUrlInvalidValues() {
//        Mockito.when(mockService.retrieve(Mockito.isNull()))
//                .thenThrow(new UrlValidationException("URL is null"));
//        Mockito.when(mockService.retrieve(""))
//                .thenThrow(new UrlValidationException("URL is empty"));
//        Mockito.when(mockService.retrieve("   "))
//                .thenThrow(new UrlValidationException("URL is blank"));
//
//        assertThrows(UrlValidationException.class, () -> controller.retrieveUrl(null));
//        assertThrows(UrlValidationException.class, () -> controller.retrieveUrl(""));
//        assertThrows(UrlValidationException.class, () -> controller.retrieveUrl("   "));
//    }
}