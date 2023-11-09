/**
 * 
 */
package may;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

//import may.yuntian.anlian.entity.GatherPlanEntity;

/**
 * @author mayong
 * Stream将要处理的元素集合看作一种流，在流的过程中，借助Stream API对流中的元素进行操作，比如：筛选、排序、聚合等。
 * Stream可以由数组或集合创建，对流的操作分为两种：
 * 中间操作，每次返回一个新的流，可以有多个。
 * 终端操作，每个流只能进行一次终端操作，终端操作结束后流无法再次使用。终端操作会产生一个新的集合或值。
 * 另外，Stream有几个特性：
 * stream不存储数据，而是按照特定的规则对数据进行计算，一般会输出结果。
 * stream不会改变数据源，通常情况下会产生一个新的集合或一个值。
 * stream具有延迟执行特性，只有调用终端操作时，中间操作才会执行。
 * https://mp.weixin.qq.com/s/13fmmjbMPcSWsr5GYdMgzA
 */
class StreamTest {

	/**
	 * @throws java.lang.Exception
	 * Stream的创建
	 */
	@BeforeEach
	void setUp() throws Exception {
//		Stream可以通过集合数组创建。
//		1、通过 java.util.Collection.stream() 方法用集合创建流
		List<String> list = Arrays.asList("a", "b", "c", "e", "f");
		// 创建一个顺序流，初始的顺序
//		System.out.println("创建一个顺序流");
		Stream<String> stream = list.stream();
//		stream.forEach(System.out::println);

		// 创建一个并行流，初始的乱顺
//		System.out.println("创建一个并行流");
		Stream<String> parallelStream = list.parallelStream();
//		parallelStream.forEach(System.out::println);


//		2、使用java.util.Arrays.stream(T[] array)方法用数组创建流
		int[] array={1,3,5,6,8};
		IntStream intStream = Arrays.stream(array);
//		System.out.println("使用java.util.Arrays.stream(T[] array)方法用数组创建流");
//		intStream.forEach(System.out::println);

//		3、使用Stream的静态方法：of()、iterate()、generate()
		System.out.println("使用Stream的静态方法：of()方法创建流");
		Stream<Integer> stream1 = Stream.of(1, 2, 3, 4, 5, 6);
//		stream1.forEach(System.out::println);

		System.out.println("使用Stream的静态方法：iterate()方法创建流");
		Stream<Integer> stream2 = Stream.iterate(0, (x) -> x + 3).limit(4);
//		stream2.forEach(System.out::println); // 0 2 4 6 8 10

		System.out.println("使用Stream的静态方法：generate()方法创建流");
		Stream<Double> stream3 = Stream.generate(Math::random).limit(3);
//		stream3.forEach(System.out::println);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void test() {
		streamTestPersonCollect();	//	筛选员工中工资高于8000的人，并形成新的集合
		
		streamTestMax();	//	获取String集合中最长的元素
		
		splitString();		//	将两个字符数组合并成一个新的字符数组
		
		reduce();			//	归约,求Integer集合的元素之和、乘积和最大值
		
		System.out.println("归集(toList/toSet/toMap)");
		toCollection();		//	归集(toList/toSet/toMap)
		
		System.out.println("接合(joining)");
		joining();//接合(joining)
		joining2();//接合(joining)
		
		String[] strArr = { "abcd", "bcdd", "defde", "fTr" };
		List<String> strList = Arrays.stream(strArr).map(String::toUpperCase).collect(Collectors.toList());
		System.out.println("每个元素大写：" + strList);//每个元素大写：[ABCD, BCDD, DEFDE, FTR]
	
		List<Integer> intList = Arrays.asList(1, 3, 5, 7, 9, 11);
		List<Integer> intListNew = intList.stream().map(x -> x + 3).collect(Collectors.toList());
		System.out.println("每个元素+3：" + intListNew);//每个元素+3：[4, 6, 8, 10, 12, 14]

	}
	
	/**
	 * 归约，也称缩减，顾名思义，是把一个流缩减成一个值，能实现对集合求和、求乘积和求最值操作。
	 * 求Integer集合的元素之和、乘积和最大值
	 */
	public static void reduce() {
		  List<Integer> list = Arrays.asList(1, 3, 2, 8, 11, 4);
		  // 求和方式1
		  Optional<Integer> sum = list.stream().reduce((x, y) -> x + y);
		  // 求和方式2
		  Optional<Integer> sum2 = list.stream().reduce(Integer::sum);
		  // 求和方式3
		  Integer sum3 = list.stream().reduce(0, Integer::sum);
		  
		  // 求乘积
		  Optional<Integer> product = list.stream().reduce((x, y) -> x * y);

		  // 求最大值方式1
		  Optional<Integer> max = list.stream().reduce((x, y) -> x > y ? x : y);
		  // 求最大值写法2
		  Integer max2 = list.stream().reduce(1, Integer::max);

		  System.out.println("list求和：" + sum.get() + "," + sum2.get() + "," + sum3);
		  System.out.println("list求积：" + product.get());
		  System.out.println("list求和：" + max.get() + "," + max2);
		 }
	
	//将两个字符数组合并成一个新的字符数组
	 public static void splitString() {
		  List<String> list = Arrays.asList("m,k,l,a","1,3,5,7");
		  
		  List<String> listNew = list.stream().flatMap(s -> {
			  System.out.println("flatMap处理：" + s);
		   // 将每个元素转换成一个stream
		   String[] split = s.split(",");
		   System.out.println("flatMap截取处理：" + Arrays.asList(split));
		   
		   Stream<String> s2 = Arrays.stream(split);
		   System.out.println("flatMap处理完成：" + Arrays.asList(s2));
		   
		   return s2;
		  }).collect(Collectors.toList());

		  System.out.println("处理前的集合：" + list);//处理前的集合：[m,k,l,a, 1,3,5,7]
		  System.out.println("处理后的集合：" + listNew);//处理后的集合：[m, k, l, a, 1, 3, 5]
	}		
	
	/**
	 * 获取String集合中最长的元素
	 * 输出
	 */
	public static void streamTestMax() {
		  List<String> list = Arrays.asList("adnm", "maxadmmt1", "pot", "xbangd", "weoujgsd");

		  Optional<String> max = list.stream().max(Comparator.comparing(String::length));
		  System.out.println("最长的字符串：" + max.get());
	}

	/**
	 * 筛选员工中工资高于8000的人，并形成新的集合。 形成新集合依赖collect（收集)
	 * 运行结果：高于8000的员工姓名：[Tom, Anni, Owen]
	 */
	public static void streamTestPersonCollect() {
		  List<Person> personList = new ArrayList<Person>();
		  personList.add(new Person("Tom", 8900, 23, "male", "New York"));
		  personList.add(new Person("Jack", 7000, 25, "male", "Washington"));
		  personList.add(new Person("Lily", 7800, 21, "female", "Washington"));
		  personList.add(new Person("Anni", 8200, 24, "female", "New York"));
		  personList.add(new Person("Owen", 9500, 25, "male", "New York"));
		  personList.add(new Person("Alisa", 7900, 26, "female", "New York"));

		  // 将员工按薪资是否高于8000分组
		Map<Boolean, List<Person>> part = personList.stream().collect(Collectors.partitioningBy(x -> x.getSalary() > 8000));
		// 将员工按性别分组
		Map<String, List<Person>> group = personList.stream().collect(Collectors.groupingBy(Person::getSex));
		

		// 将员工先按性别分组，再按地区分组
		Map<String, Map<String, List<Person>>> group3 = personList.stream().collect(Collectors.groupingBy(Person::getSex, Collectors.groupingBy(Person::getArea)));
		
		// 按年龄去重分组
		List<Integer> group4 = personList.stream().map(Person::getAge).distinct().collect(Collectors.toList());

		System.out.println("员工按薪资是否大于8000分组情况：" + part);
		System.out.println("员工按性别分组情况：" + group);
		System.out.println("员工按性别、地区：" + group3);
		System.out.println("员工按年龄去重分组：" + group4);
		
		Integer[] ages = new Integer[]{23, 21};
		Long[] ages2 = new Long[]{23L, 21L};
		
		List<Integer> group5 = personList.stream().filter(p->Arrays.asList(ages).contains(p.getAge())).map(Person::getAge).collect(Collectors.toList());
		System.out.println("员工按年龄分组 符合条件的数据21：" + Arrays.asList(ages).contains(21)+" | "+ Arrays.asList(ages2).contains(21L));
		System.out.println("员工按年龄分组 符合条件的数据：" + group5);
		
		  List<String> fiterList = personList.stream().filter(x -> x.getSalary() > 8000).map(Person::getName)
		    .collect(Collectors.toList());
		  System.out.println("高于8000的员工姓名：" + fiterList);
		  
		  List<String> fiterList2 = personList.parallelStream().filter(x -> "Tom,Jack,Anni".contains(x.getName()) ).map(Person::getName)
				  .collect(Collectors.toList());
		  System.out.println("员工姓名包含Tom,Jack,Anni的数据：" + fiterList2);
		  
		  
		// 不改变原来员工集合的方式
//		  List<Person> personListNew = personList.stream().map(person -> {
//		   Person personNew = new Person(person.getName(), 0, 0, null, null);
//		   personNew.setSalary(person.getSalary() + 10000);
//		   return personNew;
//		  }).collect(Collectors.toList());
		  System.out.println("一次改动前：" + personList.get(0).getName() + "-->" + personList.get(0).getSalary());
//		  System.out.println("一次改动后：" + personListNew.get(0).getName() + "-->" + personListNew.get(0).getSalary());

		  // 改变原来员工集合的方式
		  List<Person> personListNew2 = personList.stream().map(person -> {
		   person.setSalary(person.getSalary() + 10000);
		   return person;
		  }).collect(Collectors.toList());
		  System.out.println("二次改动前：" + personList.get(0).getName() + "-->" + personList.get(0).getSalary());
		  System.out.println("二次改动后：" + personListNew2.get(0).getName() + "-->" + personListNew2.get(0).getSalary());
		 
		 }

	/**
	 * 归集(toList/toSet/toMap)
	 * 因为流不存储数据，那么在流中的数据完成处理后，需要将流中的数据重新归集到新的集合里。
	 * toList、toSet和toMap比较常用，另外还有toCollection、toConcurrentMap等复杂一些的用法。
	 */
	public static void toCollection() {
		  List<Integer> list = Arrays.asList(1, 6, 3, 4, 6, 7, 9, 6, 20);
		  List<Integer> listNew = list.stream().filter(x -> x % 2 == 0).collect(Collectors.toList());//[6, 4, 6, 6, 20]
		  Set<Integer> set = list.stream().filter(x -> x % 2 == 0).collect(Collectors.toSet());//[4, 20, 6]
	
		  List<Person> personList = new ArrayList<Person>();
		  personList.add(new Person("Tom", 8900, 23, "male", "New York"));
		  personList.add(new Person("Jack", 7000, 25, "male", "Washington"));
		  personList.add(new Person("Lily", 7800, 21, "female", "Washington"));
		  personList.add(new Person("Anni", 8200, 24, "female", "New York"));
		  
		  Map<?, Person> map = personList.stream().filter(p -> p.getSalary() > 8000)
				  .collect(Collectors.toMap(Person::getName, p -> p));
		  System.out.println("toList:" + listNew);
		  System.out.println("toSet:" + set);
		  System.out.println("toMap:" + map);//{Tom=may.Person@7a36aefa, Anni=may.Person@17211155}
	 }
	
	/**
	 * 接合(joining)
	 * joining可以将stream中的元素用特定的连接符（没有的话，则直接连接）连接成一个字符串。
	 */
	public static void joining() {
		  List<Person> personList = new ArrayList<Person>();
		  personList.add(new Person("Tom", 8900, 23, "male", "New York"));
		  personList.add(new Person("Jack", 7000, 25, "male", "Washington"));
		  personList.add(new Person("Lily", 7800, 21, "female", "Washington"));

		  String names = personList.stream().map(p -> p.getName()).collect(Collectors.joining(","));
		  System.out.println("所有员工的姓名：" + names);
		  List<String> list = Arrays.asList("A", "B", "C");
		  String string = list.stream().collect(Collectors.joining("-"));
		  System.out.println("拼接后的字符串：" + string);
	}
	
	/**
	 * 接合(joining)
	 * joining可以将stream中的元素用特定的连接符（没有的话，则直接连接）连接成一个字符串。
	 */
	public static void joining2() {
		List<Person> personList = new ArrayList<Person>();
		personList.add(new Person("Tom", 8900, 23, "male", "苯"));
		personList.add(new Person("Jack", 7000, 25, "male", "甲苯"));
		personList.add(new Person("Lily", 7800, 21, "female", "二甲苯"));
		personList.add(new Person("Lily2", 8800, 22, "female", "正己烷"));
//		四苯两酯(苯、甲苯、二甲苯、乙苯、乙酸乙酯、乙酸丁酯)；
//		三苯两酯(苯、甲苯、二甲苯、乙酸乙酯、乙酸丁酯)；
		
//		苯 甲苯 二甲苯  乙苯  乙酸乙酯  乙酸丁酯 可以用一个碳管采样  即一起采
//		正己烷 戊烷 庚烷 辛烷 壬烷 可以一起采
//		SELECT * FROM `t_gist_method` WHERE certification IN ('正己烷','正戊烷','正庚烷','辛烷','壬烷') ;-- AND collector = '活性炭管';
//		SELECT * FROM `t_gist_method` WHERE certification IN ('苯','甲苯','二甲苯','乙苯','乙酸乙酯','乙酸丁酯');--  AND collector = '活性炭管';
		
		String  charcoal_tube_alkane = "|正己烷|正戊烷|正庚烷|辛烷|壬烷|";//可以在同一个活性炭管中采集的己烷类物质的配置
		String  charcoal_tube_benzene = "|苯|甲苯|二甲苯|乙苯|乙酸乙酯|乙酸丁酯|";//可以在同一个活性炭管中采集的苯类物质的配置
//		predicate
		String names = personList.stream().filter(predicate->charcoal_tube_benzene.contains("|"+predicate.getArea()+"|")).map(p -> p.getArea()).collect(Collectors.joining(","));
		System.out.println("所有符合元素的名称：" + names);
		List<String> list = Arrays.asList("A", "B", "C");
		String string = list.stream().collect(Collectors.joining("-"));
		System.out.println("拼接后的字符串：" + string);
		
		List charcoal_tube_personList = personList.stream().filter(predicate->charcoal_tube_benzene.contains("|"+predicate.getArea()+"|")).collect(Collectors.toList());;
		System.out.println("可以在同一个活性炭管中采集的苯类物质：" + charcoal_tube_personList);
		
		Boolean r = personList.removeAll(charcoal_tube_personList);
		System.out.println("剔除在同一个活性炭管中采集的苯类物质：" + personList);
	}
	
}

class Person {
	 private String name;  // 姓名
	 private int salary; // 薪资
	 private int age; // 年龄
	 private String sex; //性别
	 private String area;  // 地区

	 // 构造方法
	 public Person(String name, int salary, int age,String sex,String area) {
	  this.name = name;
	  this.salary = salary;
	  this.age = age;
	  this.sex = sex;
	  this.area = area;
	 }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSalary() {
		return salary;
	}

	public void setSalary(int salary) {
		this.salary = salary;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	@Override
	public String toString() {
		return "Person [name=" + name + ", salary=" + salary + ", age=" + age + ", sex=" + sex + ", area=" + area + "]";
	}
	 
}