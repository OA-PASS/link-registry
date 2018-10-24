# Link registry

[![Build Status](https://travis-ci.org/OA-PASS/link-registry.svg?branch=master)](https://travis-ci.org/OA-PASS/link-registry)

Provides a registry of URIs mapped to short, unique keys.  Includes an HTTP URI resolution services for redirecting "permalinks" based on these keys to the registered URIs.

Characteristics of these keys include:

* Short enough to avoid annoyingly-long URIs
* Random and non-guessable.

The primary use case for such a registry/service is for producing non-guessable shortened invitation links to send in e-mails.
