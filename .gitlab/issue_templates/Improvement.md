# Improvement
<!--
  This is for improvements for existing features which may
  be a small optimisation of compliance related change, but
  doesn't add any significant functionality.

  This is pre-filled with example values, feel free to
  remove them before populating the template.
  
  If you feel a heading is irrelevent, just remove it.
-->

## Description
<!-- 
  Explain what this is about, try to use full sentences, and make your point clear.
-->
Alexis currently has Google Translate in it, and the attribution image sent with
messages relies only on a configuration which users must define, otherwise no
attribution is given.

It would be better to attach and send the attribution internally if the
`alexis.translate.attribution-url` is not defined in the configuration file.

## Motivation
This would be helpful becasue it helps ensure compliance with the
Google Cloud Translate Attribution Requirement guidelines and discourage
other users from breaching it, especially when using this code base 
gives the project an assiociation with this regardless of who hosts it.
