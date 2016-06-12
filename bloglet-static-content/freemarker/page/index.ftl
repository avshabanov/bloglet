<#import "../template/page-wrapper.ftl" as pw/>
<#import "../template/posts.ftl" as p/>

<@pw.page title="Main">

<h2>Blog Posts</h2>

<div id="post-container" class="container">
  <@p.blogPosts postList=posts />
</div>

<div class="container load-more-container">
  <p>Click 'Load More' to fetch more books</p>
  <div>
    <button id="btn-load-more" type="button" class="btn btn-primary">Load More</button>
  </div>
</div>

</@pw.page>
