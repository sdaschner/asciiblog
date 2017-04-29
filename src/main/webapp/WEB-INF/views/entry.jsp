<%@ page contentType="text/html;charset=UTF-8" language="java" %><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ taglib prefix="blog" uri="/WEB-INF/blog.tld" %><!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content=""/>
    <meta name="keywords" content=""/>
    <title>${entry.title} - Sebastian Daschner</title>
    <link rel="stylesheet" href="${homeUri}/css/style.css"/>
    <link rel="alternate" type="application/rss+xml" title="Blog - Sebastian Daschner" href="/feeds/rss"/>
</head>
<body>
    <div id="content">
        <header>
            <h1>
                <a href="/"><strong>sebastiandaschner</strong> blog</a>
            </h1>
            <hr>
            <ul>
                <li>
                    <a href="${homeUri}/about">About</a>
                </li>
                <li>
                    <a href="${homeUri}/offerings">Offerings</a>
                </li>
                <li>
                    <a href="${homeUri}/contact">Contact</a>
                </li>
                <li>
                    <a href="/">Blog</a>
                </li>
                <li>
                    <a href="${homeUri}/news">News</a>
                </li>
            </ul>
        </header>
        <main>
            <h2>${entry.title}</h2>
            <span class="date"><blog:formatDate value="${entry.date}" /></span>
            ${entry.content}

            <p>&nbsp;</p>
            <p>Did you like the post? Subscribe to my newsletter for more free content, tips and tricks on IT &amp; Java:</p>
            <script src="https://assets.convertkit.com/assets/CKJS4.js?v=21"></script>
            <div class="ck_form_container ck_inline" data-ck-version="6">
                <div class="ck_form ck_naked">
                    <div class="ck_form_fields">
                        <div id="ck_success_msg" style="display:none;">
                            <p>Success! Now check your email to confirm your subscription.</p>
                        </div>

                        <!--  Form starts here  -->
                        <form id="ck_subscribe_form" class="ck_subscribe_form" action="https://app.convertkit.com/landing_pages/168744/subscribe" data-remote="true">
                            <input type="hidden" value="{&quot;form_style&quot;:&quot;naked&quot;,&quot;embed_style&quot;:&quot;inline&quot;,&quot;embed_trigger&quot;:&quot;scroll_percentage&quot;,&quot;scroll_percentage&quot;:&quot;70&quot;,&quot;delay_seconds&quot;:&quot;10&quot;,&quot;display_position&quot;:&quot;br&quot;,&quot;display_devices&quot;:&quot;all&quot;,&quot;days_no_show&quot;:&quot;15&quot;,&quot;converted_behavior&quot;:&quot;show&quot;}" id="ck_form_options">
                            <input type="hidden" name="id" value="168744" id="landing_page_id">
                            <div class="ck_errorArea">
                                <div id="ck_error_msg" style="display:none">
                                    <p>There was an error submitting your subscription. Please try again.</p>
                                </div>
                            </div>
                            <div class="ck_control_group ck_email_field_group">
                                <label class="ck_label" for="ck_emailField" style="display: none">Email Address</label>
                                <input type="text" name="first_name" class="ck_first_name" id="ck_firstNameField" placeholder="First Name">
                                <input type="email" name="email" class="ck_email_address" id="ck_emailField" placeholder="Email Address" required>
                            </div>
                            <div class="ck_control_group ck_captcha2_h_field_group ck-captcha2-h" style="position: absolute !important;left: -999em !important;">
                                <input type="text" name="captcha2_h" class="ck-captcha2-h" id="ck_captcha2_h" placeholder="We use this field to detect spam bots. If you fill this in, you will be marked as a spammer.">
                            </div>

                            <button class="subscribe_button ck_subscribe_button btn fields" id="ck_subscribe_button">
                                Subscribe
                            </button>
                        </form>
                    </div>
                </div>
            </div>

        </main>
    </div>
    <footer>
        <span>&copy; Sebastian Daschner, CC BY-NC-SA 4.0</span>
    </footer>
</body>
</html>