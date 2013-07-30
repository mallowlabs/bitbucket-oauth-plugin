Jenkins Bitbucket OAuth Plugin
============================

Overview
--------
This Jenkins plugin enables [OAuth](http://oauth.net) authentication for [Bitbucket](https://bitbucket.org) users.

Bitbucket Security Realm (authentication):
--------------------------------------------

First you need to get consumer key/secret from Bitbucket.

1. Log into your Bitbucket account.
2. Click accountname > **Manage Account** from the menu bar.
   The Account settings page appears.
3. Click **Integrated applications** from the menu bar.
4. Click the **Add consumer** button.
5. The system requests the following information:
   **Name** is required. Others are optional.
6. Press **Add consumer**.
   The system generates a key and a secret for you.
   Toggle the consumer name to see the generated Key and Secret value for your consumer.

Second, you need to configure your Jenkins.

1. Open Jenkins **Configure System** page.
2. Set correct URL to **Jenkins URL**
3. Click **Save** button.
4. Open Jenkins **Configure Global Security** page.
5. Check **Enable security**.
6. Select **Bitbucket OAuth Plugin** in **Security Realm**.
7. Input your Consumer Key to **Client ID**.
8. Input your Consumer Secret to **Client Secret**.
9. Click **Save** button.

Credits
-------
This plugin reuses many codes of [Jenkins Assembla Auth Plugin](https://wiki.jenkins-ci.org/display/JENKINS/Assembla+Auth+Plugin).
Many thanks to Assembla team.


License
-------

	(The MIT License)

	Copyright (c) 2013 mallowlabs

	Permission is hereby granted, free of charge, to any person obtaining
	a copy of this software and associated documentation files (the
	'Software'), to deal in the Software without restriction, including
	without limitation the rights to use, copy, modify, merge, publish,
	distribute, sublicense, and/or sell copies of the Software, and to
	permit persons to whom the Software is furnished to do so, subject to
	the following conditions:

	The above copyright notice and this permission notice shall be
	included in all copies or substantial portions of the Software.

	THE SOFTWARE IS PROVIDED 'AS IS', WITHOUT WARRANTY OF ANY KIND,
	EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
	MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
	IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
	CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
	TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
	SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

