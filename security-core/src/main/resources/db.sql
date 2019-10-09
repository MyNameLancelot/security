-- 记住我功能使用的建表语句
create table persistent_logins (username varchar(64) not null,
                                series varchar(64) primary key,
                                token varchar(64) not null,
                                last_used timestamp not null)

-- 社交登陆功能使用的建表语句
create table userconnection (
  userId varchar(255) NOT NULL,
  providerId varchar(255) NOT NULL,
  providerUserId varchar(255) NOT NULL,
  rank int(11) NOT NULL,
  displayName varchar(255) DEFAULT NULL,
  profileUrl varchar(512) DEFAULT NULL,
  imageUrl varchar(512) DEFAULT NULL,
  accessToken varchar(512) NOT NULL,
  secret varchar(512) DEFAULT NULL,
  refreshToken varchar(512) DEFAULT NULL,
  expireTime bigint(20) DEFAULT NULL,
  PRIMARY KEY (userId,providerId,providerUserId),
  UNIQUE KEY UserConnectionRank (userId,providerId,rank),
  UNIQUE KEY ProviderOpenId (providerId,providerUserId)  -- 如果一个第三方账户只允许绑定一个系统账户需要加入此句，否则删除
)