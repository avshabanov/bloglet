<#import "../template/page-wrapper.ftl" as pw/>
<#import "../template/posts.ftl" as p/>

<@pw.page title="Post">

<h2>${post.contents.title}</h2>

<div id="post-contents">
  ${post.contents.contents}
</div>

</@pw.page>
