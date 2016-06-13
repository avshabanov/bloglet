<#import "../template/page-wrapper.ftl" as pw/>
<#import "../template/posts.ftl" as p/>

<@pw.page title="Tag">

<h2>${tag.name}</h2>

<div id="post-container" class="container">
  <@p.blogPosts postList=posts />
</div>

<div class="container load-more-container">
  <button id="btn-load-more-posts-for-tag" type="button" class="btn btn-primary">Load More</button>
</div>

</@pw.page>
