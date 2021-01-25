function FindProxyForURL(url, host) {
    if  (
        shExpMatch(host, "*ashiato.travel.rakuten.co*") ||
        shExpMatch(host, "*trvimg.r10s.jp*") ||
        shExpMatch(host, "*simg.travel.rakuten.co.jp*") ||
        shExpMatch(host, "*img.travel.rakuten.co.jp*") ||
        shExpMatch(host, "*tech.mytrip.net*") ||
        shExpMatch(host, "*zip.api.travel.rakuten.co.jp*") 
        )
        return "DIRECT";
    if (
        shExpMatch(host, "*manage.travel.rakuten.co.jp*") ||
        shExpMatch(host, "*stg-group.rwiths.net*") ||
        shExpMatch(host, "*stg-hotel.rwiths.net*") ||
        shExpMatch(host, "*cars.travel.rakuten.co.jp*") ||
        shExpMatch(host, "*ntpf.rwiths.net*") ||
        shExpMatch(host, "*grp0*.id.rakuten.co.jp*") ||
        shExpMatch(host, "*rsvh.travel.rakuten.co.jp*") ||
        shExpMatch(host, "*aps1.travel.rakuten.co.jp*") ||
        shExpMatch(host, "*kw.travel.rakuten.co.jp*") ||
        shExpMatch(host, "*grp0*.m.rakuten.co.jp*") ||
        shExpMatch(host, "*hotel.travel.rakuten.co.jp*") ||
        shExpMatch(host, "*web.travel.rakuten.co.jp*") ||
        shExpMatch(host, "osearch.travel.rakuten.co.jp*") ||
        shExpMatch(host, "*rd.rakuten.co.jp*") ||
        shExpMatch(host, "*dp.travel.rakuten.co.jp*") ||
        shExpMatch(host, "*img.travel.rakuten.co.jp/share/img_cars/enterprise*") ||
        shExpMatch(host, "*cars.travel.rakuten.co.jp/rax/uploadMap.do*") ||
        shExpMatch(host, "*collabo.rakuten.co.jp*") ||
        shExpMatch(host, "*mente.mytrip.net*") ||
        shExpMatch(host, "*bookjp.api.travel.rakuten.co.jp*") ||
        shExpMatch(host, "*my.travel.rakuten.co.jp*") ||
        shExpMatch(host, "*m.travel.rakuten.co.jp*") ||
        shExpMatch(host, "*.mytrip.net*") ||
        shExpMatch(host, "*travel.collabo.rakuten.co.jp*") ||
        shExpMatch(host, "*search.travel.rakuten.co.jp*") ||
        shExpMatch(host, "*tech.travel.rakuten.co.jp*") ||
        shExpMatch(host, "*pet.travel.rakuten.co.jp*") ||
        shExpMatch(host, "*pet.gl.travel.rakuten.co.jp*") ||
        shExpMatch(host, "*travel.rakuten.co.jp*") ||
        shExpMatch(host, "*stgci.travel.rakuten.co.jp*") ||
        shExpMatch(host, "*travel.rakuten.com*") ||
        shExpMatch(host, "*travel.rakuteten.com.tw*") ||
        shExpMatch(host, "*travel.rakuteten.co.kr*") ||
        shExpMatch(host, "*img.travel.rakuten.co.jp*") ||
        shExpMatch(host, "*book.travel.rakuten.com*") ||
        shExpMatch(host, "*mente.travel.rakuten.co.jp*") ||
        shExpMatch(host, "*book.travel.rakuten.com.tw*") ||
        shExpMatch(host, "*travel.rakuten.co.kr*") ||
        shExpMatch(host, "*book.travel.rakuten.co.kr*") ||
        shExpMatch(host, "*big5.tabimado.net*") ||
        shExpMatch(host, "*www.travel.ralkuten.co.kr*") ||
        shExpMatch(host, "*travel.rakuten.com.hk*") ||
        shExpMatch(host, "*book.travel.rakuten.com.hk*") ||
        shExpMatch(host, "*travel.rakuten.cn*") ||
        shExpMatch(host, "*book.travel.rakuten.cn*") ||
        shExpMatch(host, "*travel.rakuten.co.th*") ||
        shExpMatch(host, "*book.travel.rakuten.co.th*") ||
        shExpMatch(host, "*travel.rakuten.fr*") ||
        shExpMatch(host, "*travel.rakuten.co.id*") ||
        shExpMatch(host, "*travel.rakuten.com.sg*") ||
        shExpMatch(host, "*travel.rakuten.com.my*") ||
        shExpMatch(host, "*book.travel.rakuten.fr*") ||
        shExpMatch(host, "*book.travel.rakuten.co.id*") ||
        shExpMatch(host, "*book.travel.rakuten.com.sg*") ||
        shExpMatch(host, "*book.travel.rakuten.com.my*") ||
        shExpMatch(host, "*grid.travel.rakuten.com.sg*") ||
        shExpMatch(host, "*api.coupon.rakuten.co.jp*") ||
        shExpMatch(host, "*ap.accounts.global.rakuten.com*")
       )
         return "PROXY 133.237.41.8:9002";
    else
        return "DEFAULT";
}