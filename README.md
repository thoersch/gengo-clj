Gengo Clojure Library (for the [Gengo API](http://developers.gengo.com/))
===========

This clj library provides access to the [Gengo API](http://developers.gengo.com/) for ordering and managing your translations.

## Building

With [Leiningen](http://github.com/technomancy/leiningen) installed:

    lein deps && lein jar

## Installing

The gengo-clj library can be installed as a dependency from [Clojars](http://clojars.org/gengo-clj)

## Usage

    (use 'gengoclj.gengo)

    (def is-sandbox? true)

    (with-gengo ["gengo-key" "gengo-secret" is-sandbox?]
      (get-order-jobs 123456789))

with-gengo macro is going to give you the wrapper for running any command you need to within gengo-clj.

Here is the list of supported functions:

 * get-account-stats []
 * get-account-balance []
 * get-account-preferred-translators []
 * post-translation-jobs [jobs as-group?]
 * revise-translation-job [job-id comment]
 * approve-translation-job [job-id rating translator-comments gengo-comments is-public?]
 * reject-translation-job [job-id reason comments captcha requeue?]
 * get-translation-job [job-id]
 * get-translation-jobs [& [job-ids]]
 * post-translation-job-comment [job-id comments]
 * get-translation-job-comments [job-id]
 * get-translation-job-feedback [job-id]
 * get-translation-job-revisions [job-id]
 * get-translation-job-revision [job-id revision-id]
 * delete-translation-job [job-id]
 * get-service-languages []
 * get-service-language-pairs [&[source-lang-code]]
 * determine-translation-cost [jobs]
 * get-order-jobs [order-id]


There's also a struct for a job since it's a common argument:

    (defstruct job :slug :body_src :lc_src :lc_tgt :tier)

It contains only the required parts, but other options include:
    :force :comment :use_preferred :callback_url :auto_approve :custom_data :position :glossary_id :max_char

## Questions or Comments

 * email: thoersch@gmail.com

 If you come across any issues, please file them on the [Github project issue tracker](https://github.com/thoersch/gengo-clj/issues).

## Documentation

Check out the full [Gengo API Documentation](http://developers.gengo.com/).

## License

Copyright © 2014 Tyler Hoersch

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
