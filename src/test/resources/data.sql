--Password encrypted using https://www.javainuse.com/onlineBcrypt
insert into local_user (name,password,email,isVerified)
values ("userA","$2a$10$j2baRJCs.pLJJItV87qWIORRLkKpV1bf2PlZ7Ho0dzT2AvAGEnGVy","userA@junit.com",true)
, ("userB","$2a$10$j2baRJCs.pLJJItV87qWIORRLkKpV1bf2PlZ7Ho0dzT2AvAGEnGVy","userB@junit.com",false)
