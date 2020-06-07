desc UserPojo;
SHOW INDEXES FROM ProductPojo;
SELECT DISTINCT
    TABLE_NAME,
    INDEX_NAME
FROM INFORMATION_SCHEMA.STATISTICS
WHERE TABLE_SCHEMA = 'pos';

insert into UserPojo(email,password,role) values("dheeraj","1234","admin");	
select * from BrandPojo;
insert into EmployeePojo(id,age,name) values(1,21,"dheeraj");
select * from EmployeePojo;
select * from BrandPojo;
select * from ProductPojo;
select * from TempClass;
select * from InventoryPojo;
select * from OrderPojo;
select * from OrderItemPojo;
CREATE TABLE users (
    id INT(4) NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL UNIQUE,
    birth_date DATE NOT NULL,
    created_at DATETIME ,
    updated_at TIMESTAMP 
);
select max(id) from users;
select p.productId,sum(p.quantity),sum(p.sellingPrice) from OrderItemPojo as p where orderId in (select OrderPojo.id from OrderPojo where date between '2020-01-16' and '2020-01-20') group by productId having productId in (select id from ProductPojo where brandId in (select id from BrandPojo where brandName='lenovo' and brandCategory='laptops'));
select b.brandName,b.brandCategory,sum(i.quantity) from BrandPojo b,ProductPojo p,InventoryPojo i where b.id=p.brandId and p.id=i.id group by b.id;

select b.brandName,b.brandCategory,sum(o.quantity),sum(o.sellingPrice) from OrderItemPojo o,BrandPojo b,ProductPojo p  where p.brandId=b.id and p.id=o.productId and o.orderId in (select ord.id from OrderPojo ord where ord.date between '2020-01-19' and '2020-01-26') group by b.id having b.brandName="lenovo";




