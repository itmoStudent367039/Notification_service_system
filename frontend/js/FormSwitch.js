$(document).ready(function () {
  $(".generalBlock").on("click", ".tab", function () {
    $(".generalBlock").find(".active").removeClass("active");
    $(this).addClass("active");
    $(".tab-form").eq($(this).index()).addClass("active");
  });
});
