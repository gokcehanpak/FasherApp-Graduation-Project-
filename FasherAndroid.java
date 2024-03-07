 
 
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
 
   private Context context;
   private List<Comments> commentsList;
   private FirebaseUser firebaseUser;
 
   public CommentAdapter(Context context, List<Comments> commentsList) {
   	this.context = context;
   	this.commentsList = commentsList;
   }
 
   @NonNull
   @Override
   public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
   	View view = LayoutInflater.from(context).inflate(R.layout.comments_item,parent,false);
   	return new CommentAdapter.ViewHolder(view);
   }
 
   @Override
   public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
   	firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
   	final Comments comment =  commentsList.get(position);
tasarımlara basılmatadır.
   	getUserInfo(holder.imageView,holder.itemusername,comment.getPublisher(),comment.getTime());
       holder.comment.setText(comment.getComment());
   	holder.comment.setOnClickListener(new View.OnClickListener() {
       	@Override
       	public void onClick(View v) {
           	Intent intent = new Intent(context, HomeActivity.class);
           	intent.putExtra("publisherid",comment.getPublisher());
           	context.startActivity(intent);
       	}
   	});
   	holder.imageView.setOnClickListener(new View.OnClickListener() {
       	@Override
       	public void onClick(View v) {
           	Intent intent = new Intent(context, HomeActivity.class);
               intent.putExtra("publisherid",comment.getPublisher());
           	context.startActivity(intent);
       	}
   	});
 
   }
 
   @Override
   public int getItemCount() {
   	return commentsList.size();
   }
 
   public class ViewHolder extends RecyclerView.ViewHolder{
 
   	private TextView itemusername,comment;
   	private CircleImageView imageView;
   	public ViewHolder(@NonNull View itemView) {
       	super(itemView);
       	itemusername = itemView.findViewById(R.id.username);
       	imageView = itemView.findViewById(R.id.imageProfile);
       	comment = itemView.findViewById(R.id.comment);
   	}
   }
 
   private void getUserInfo(final ImageView imageView,TextView userName,String publisherId,String time){
   	DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(publisherId);
       databaseReference.addValueEventListener(new ValueEventListener() {
       	@Override
       	public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
           	User user = dataSnapshot.getValue(User.class);
           	Glide.with(context).load(user.getImageurl()).into(imageView);
               userName.setText(user.getUsername() + " " +time);
 
       	}
 
       	@Override
       	public void onCancelled(@NonNull DatabaseError databaseError) {
 
       	}
   	});
   }
}
 
 
 
NotificationAdapter
Bildirimlerin liste biçiminde gösterilmesi için oluşturulmuştur.Her listenin elemanı ve tasarımı bu liste üzerinde oluşturulmuş ve kullanıcıya gösterilmiştir.
 
public class NotificationAdapter  extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
   private Context context;
 
   private List<Notifications> notificationsList;
   public NotificationAdapter(Context context, List<Notifications> notifationList) {
   	this.context = context;
   	this.notificationsList = notifationList;
   }
   @NonNull
   @Override
   public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
   	View view = LayoutInflater.from(context).inflate(R.layout.notification_item, parent, false);
   	return new NotificationAdapter.ViewHolder(view);
   }
 
   @Override
   public void onBindViewHolder(@NonNull NotificationAdapter.ViewHolder holder, int position) {
eleman xml tarafında oluşturulan  tasarıma eklenmiştir.
   	Notifications notifications = notificationsList.get(position);
       holder.comment.setText(notifications.getText());
       getUserInfo(holder.imageProfile,holder.userName,notifications.getUserid());
.
   	if(notifications.isIspost()){
       	holder.postImage.setVisibility(View.VISIBLE);
           getPostImage(holder.postImage,notifications.getPostid());
   	}else{
       	holder.postImage.setVisibility(View.GONE);
   	}
 
   	holder.itemView.setOnClickListener(new View.OnClickListener() {
       	@Override
       	public void onClick(View v) {
           	if(notifications.isIspost()){
               	PreferencesUtil.getInstance(context).putKeyValue("postid",notifications.getPostid());
                   ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,new PostDetailFragment()).commit();
           	}else{
               	PreferencesUtil.getInstance(context).putKeyValue(PreferencesUtil.PROFILE_ID,notifications.getUserid());
                   ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,new UserFragment()).commit();
          	 }
 
       	}
   	});
   }
 
   @Override
   public int getItemCount() {
   	return notificationsList.size();
   }
 
   public class ViewHolder extends RecyclerView.ViewHolder {
 
   	private ImageView imageProfile, postImage;
   	private TextView userName,comment;
   	public ViewHolder(@NonNull View itemView) {
       	super(itemView);
       	imageProfile = itemView.findViewById(R.id.imageProfile);
       	postImage = itemView.findViewById(R.id.postImage);
       	userName = itemView.findViewById(R.id.username);
       	comment = itemView.findViewById(R.id.comment);
   	}
   }
 
   private void  getUserInfo(final ImageView imageView,TextView username,String publisherid){
   	DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(publisherid);
       databaseReference.addValueEventListener(new ValueEventListener() {
       	@Override
       	public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
           	User user= dataSnapshot.getValue(User.class);
           	Glide.with(context).load(user.getImageurl()).into(imageView);
               username.setText(user.getUsername() );
 
       	}
 
       	@Override
       	public void onCancelled(@NonNull DatabaseError databaseError) {
 
       	}
   	});
   }
   private void getPostImage(ImageView imageView,String postid){
   	DatabaseReference databaseReference =  FirebaseDatabase.getInstance().getReference("Posts").child(postid);
       databaseReference.addValueEventListener(new ValueEventListener() {
       	@Override
       	public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
           	Post post = dataSnapshot.getValue(Post.class);
           	Glide.with(context).load(post.getPostimage()).into(imageView);
       	}
 
       	@Override
       	public void onCancelled(@NonNull DatabaseError databaseError) {
 
       	}
   	});
   }
}
 
 
PhotoAdapter
 
 
public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {
   private Context context;
   private List<Post> postList;
   private FirebaseUser firebaseUser;
 
   public PhotoAdapter(Context context, List<Post> postList) {
   	this.context = context;
   	this.postList = postList;
   }
 
   @NonNull
   @Override
   public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
   	View view = LayoutInflater.from(context).inflate(R.layout.photo_item,parent,false);
   	return new PhotoAdapter.ViewHolder(view);
   }
 
   @Override
   public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
   	firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
   	final Post post =  postList.get(position);
   	Glide.with(context).load(post.getPostimage()).into(holder.imageView);
   	holder.imageView.setOnClickListener(new View.OnClickListener() {
       	@Override
       	public void onClick(View v) {
           	PreferencesUtil.getInstance(context).putKeyValue("postid",post.getPostid());
               ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,new PostDetailFragment()).commit();
       	}
   	});
 
   }
 
   @Override
   public int getItemCount() {
   	return postList.size();
   }
 
   public class ViewHolder extends RecyclerView.ViewHolder{
 
   	private ImageView imageView;
 
   	public ViewHolder(@NonNull View itemView) {
       	super(itemView);
    	   imageView = itemView.findViewById(R.id.image);
   	}
   }
 
 
}
 
 
public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
   public Context context;
   public List<Post> postList;
   int colored;
   int color;
   private String starText="İşyeri";
   private AlertDialog dialogStart;
   private List<BarEntry> barEntryList;
   private List<String>  labelName;
   private List<TwoData> twoDataList;
   private FirebaseUser firebaseUser;
 
   public PostAdapter(Context context , List<Post> postList,boolean graphics) {
   	this.context = context;
   	this.postList = postList;
   	this.graphics = graphics;
   	barEntryList = new ArrayList<>();
   	labelName = new ArrayList<>();
   }
 
   @NonNull
   @Override
   public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
   	View view = LayoutInflater.from(context).inflate(R.layout.post_item,parent,false);
   	return new ViewHolder(view);
   }
 
   @Override
   public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
   	firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
   	Post post = postList.get(position);
   	colored = Color.rgb(255,27,79);
   	color =  Color.rgb(32,32,32);
   	Glide.with(context).load(post.getPostimage())
           	.apply(new RequestOptions().placeholder(R.drawable.ic_placeholder)).into(holder.postImage);
   	if(post.getDescription().isEmpty()){
           holder.description.setVisibility(View.GONE);
   	}else{
           holder.description.setVisibility(View.VISIBLE);
 	      holder.description.setText(post.getDescription());
   	}
       if(post.getCategory().equals("myfashion")){
       	holder.like.setVisibility(View.VISIBLE);
       	holder.dislike.setVisibility(View.VISIBLE);
       	holder.start.setVisibility(View.GONE);
       	holder.shop.setVisibility(View.GONE);
       	holder.dontshop.setVisibility(View.GONE);
   	}else{
       	holder.shop.setVisibility(View.VISIBLE);
       	holder.dontshop.setVisibility(View.VISIBLE);
       	holder.like.setVisibility(View.GONE);
       	holder.dislike.setVisibility(View.GONE);
       	holder.start.setVisibility(View.VISIBLE);
   	}
 
   	holder.username.setOnClickListener(new View.OnClickListener() {
       	@Override
       	public void onClick(View v) {
           	Intent intent = new Intent(context, HomeActivity.class);
               intent.putExtra("publisherid",post.getPublisher());
           	context.startActivity(intent);
       	}
   	});
 
       holder.imageProfile.setOnClickListener(new View.OnClickListener() {
       	@Override
       	public void onClick(View v) {
           	Intent intent = new Intent(context, HomeActivity.class);
               intent.putExtra("publisherid",post.getPublisher());
           	context.startActivity(intent);
       	}
   	});
 
       publisherInfo(holder.imageProfile,holder.username,holder.publisher,post.getPublisher());
   	if(post.getCategory().equals("myfashion")){
       	isLiked(post.getPostid(),holder.like,holder.dislike);
           isDisLiked(post.getPostid(),holder.dislike,holder.like);
           nrLikes(holder.likes,holder.dontlikes,post.getPostid()," Like"," Dislike","Likes","Dislikes",holder.barChart);
   	}else{
           isShop(post.getPostid(),holder.shop,holder.dontshop);
           isDontShop(post.getPostid(),holder.dontshop,holder.shop);
           nrLikes(holder.likes,holder.dontlikes,post.getPostid()," Shop"," Dont Shop","Shops","Dontshops",holder.barChart);
           isStart(post.getPostid(),holder.start,holder.barChart);
   	}
   	getComments(holder.comments,post.getPostid());
    	holder.like.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View v) {
                if(holder.like.getTag().equals("like")){
                	FirebaseDatabase.getInstance().getReference().child("Likes").child(post.getPostid()).child(firebaseUser.getUid()).setValue(true);
                    addNotification(post.getPublisher(),post.getPostid(),"Image Liked");
                	holder.dislike.setEnabled(false);
            	}else{
                	FirebaseDatabase.getInstance().getReference().child("Likes").child(post.getPostid()).child(firebaseUser.getUid()).removeValue();
                    holder.dislike.setEnabled(true);
 
            	}
	        }
    	});
    	holder.dislike.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View v) {
                if(holder.dislike.getTag().equals("dislike")){
           	     FirebaseDatabase.getInstance().getReference().child("Dislikes").child(post.getPostid()).child(firebaseUser.getUid()).setValue(true);
                    holder.like.setEnabled(false);
                    addNotification(post.getPublisher(),post.getPostid(),"Image Disliked");
            	}else{
                	FirebaseDatabase.getInstance().getReference().child("Dislikes").child(post.getPostid()).child(firebaseUser.getUid()).removeValue();
                    holder.like.setEnabled(true);
  	          }
        	}
    	});
   	holder.shop.setOnClickListener(new View.OnClickListener() {
       	@Override
       	public void onClick(View v) {
               if(holder.shop.getTag().equals("shop")){
       	        FirebaseDatabase.getInstance().getReference().child("Shops").child(post.getPostid()).child(firebaseUser.getUid()).setValue(true);
                   addNotification(post.getPublisher(),post.getPostid(),"Added Shop");
               	holder.dontshop.setEnabled(false);
 
           	}else{
               	FirebaseDatabase.getInstance().getReference().child("Shops").child(post.getPostid()).child(firebaseUser.getUid()).removeValue();
                   holder.dontshop.setEnabled(true);
     	      }
       	}
   	});
   	holder.dontshop.setOnClickListener(new View.OnClickListener() {
       	@Override
       	public void onClick(View v) {
               if(holder.dontshop.getTag().equals("dontshop")){
 	              FirebaseDatabase.getInstance().getReference().child("Dontshops").child(post.getPostid()).child(firebaseUser.getUid()).setValue(true);
                   addNotification(post.getPublisher(),post.getPostid(),"Dont added Shop");
            	   holder.shop.setEnabled(false);
           	}else{
               	FirebaseDatabase.getInstance().getReference().child("Dontshops").child(post.getPostid()).child(firebaseUser.getUid()).removeValue();
                   holder.shop.setEnabled(true);
           	}
       	}
   	});
   	holder.start.setOnClickListener(new View.OnClickListener() {
       	@Override
       	public void onClick(View v) {
               	AlertDialog.Builder builder = new AlertDialog.Builder(context);
                   builder.setView(setChooseAlertDialog(starText,post.getPostid(),holder.start));
               	dialogStart = builder.create();
               	dialogStart.show();
 
       	}
   	});
         holder.comment.setOnClickListener(new View.OnClickListener() {
       	@Override
       	public void onClick(View v) {
 
           	Intent intent = new Intent(context, CommentsActivity.class);
               intent.putExtra("postid",post.getPostid());
               intent.putExtra("publisherid",post.getPublisher());
           	context.startActivity(intent);
       	}
   	});
   	holder.comments.setOnClickListener(new View.OnClickListener() {
       	@Override
       	public void onClick(View v) {
           	Intent intent = new Intent(context, CommentsActivity.class);
               intent.putExtra("postid",post.getPostid());
               intent.putExtra("publisherid",post.getPublisher());
         	  context.startActivity(intent);
       	}
   	});
   }
 
   private  void getComments(TextView comments, String postId){
   	DatabaseReference databaseReference =  FirebaseDatabase.getInstance().getReference().child("Comments").child(postId);
       databaseReference.addValueEventListener(new ValueEventListener() {
       	@Override
       	public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               comments.setText(dataSnapshot.getChildrenCount() + (dataSnapshot.getChildrenCount() >1 ? " Comments":" Comment"));
       	}
 
       	@Override
       	public void onCancelled(@NonNull DatabaseError databaseError) {
 
       	}
   	});
   }
   private  void  graphics(List<TwoData> twoDataList,String graphicName,String descriptions,BarChart barChart){
   	barChart.setVisibility(View.VISIBLE);
   	if(twoDataList != null && !twoDataList.isEmpty()){
       	barEntryList.clear();
       	labelName.clear();
       	for(int i=0;i<twoDataList.size();i++){
           	String name = twoDataList.get(i).getName();
           	int value = twoDataList.get(i).getNumber();
           	barEntryList.add(new BarEntry(i,value));
           	labelName.add(name);
       	}
   	}
   	BarDataSet barDataSet = new BarDataSet(barEntryList, graphicName );
   	barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
   	Description description =  new Description();
   	description.setText(descriptions);
   	barChart.setDescription(description);
   	BarData barData = new BarData(barDataSet);
   	barChart.setData(barData);
   	XAxis xAxis = barChart.getXAxis();
   	xAxis.setValueFormatter(new IndexAxisValueFormatter(labelName));
   	xAxis.setPosition(XAxis.XAxisPosition.TOP);
   	xAxis.setDrawGridLines(false);
   	xAxis.setDrawAxisLine(false);
   	xAxis.setGranularity(1f);
   	xAxis.setLabelCount(labelName.size());
   	xAxis.setLabelRotationAngle(270);
   	barChart.animateY(2000);
   	barChart.invalidate();
 
   }
   @Override
   public int getItemCount() {
   	return postList.size();
   }
   public class ViewHolder extends RecyclerView.ViewHolder{
   	public ImageView imageProfile,postImage,like,dislike,shop,dontshop,comment,start;
   	public TextView username,likes,publisher,description,comments,dontlikes;
   	public BarChart barChart;
 
   	public ViewHolder(@NonNull View itemView) {
       	super(itemView);
       	imageProfile = itemView.findViewById(R.id.imageProfile);
       	postImage = itemView.findViewById(R.id.postImage);
       	like = itemView.findViewById(R.id.like);
       	dislike = itemView.findViewById(R.id.dislike);
       	shop = itemView.findViewById(R.id.shop);
       	dontshop = itemView.findViewById(R.id.dontshop);
  	     comment = itemView.findViewById(R.id.comment);
       	start = itemView.findViewById(R.id.start);
       	username = itemView.findViewById(R.id.username);
       	likes = itemView.findViewById(R.id.likes);
       	dontlikes = itemView.findViewById(R.id.dontlikes);
       	publisher = itemView.findViewById(R.id.publisher);
       	description = itemView.findViewById(R.id.descriptions);
       	comments = itemView.findViewById(R.id.comments);
       	barChart = itemView.findViewById(R.id.barchart);
 
 
   	}
   }
   private void isLiked(String postid,ImageView imageView,ImageView enabledView){
   	FirebaseUser firebaseUser =  FirebaseAuth.getInstance().getCurrentUser();
   	DatabaseReference databaseReference =  FirebaseDatabase.getInstance().getReference().child("Likes").child(postid);
   	databaseReference.addValueEventListener(new ValueEventListener() {
       	@Override
       	public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               if(dataSnapshot.child(firebaseUser.getUid()).exists()){
               	ImageViewCompat.setImageTintList(imageView, ColorStateList.valueOf(colored));
                   imageView.setTag("liked");
                   enabledView.setEnabled(false);
           	}else{
               	ImageViewCompat.setImageTintList(imageView, ColorStateList.valueOf(color));
                   imageView.setTag("like");
                   enabledView.setEnabled(true);
           	}
       	}
 
       	@Override
       	public void onCancelled(@NonNull DatabaseError databaseError) {
 
       	}
   	});
 
   }
 
 
   private void isDisLiked(String postid, ImageView imageView, ImageView enabledView){
   	FirebaseUser firebaseUser =  FirebaseAuth.getInstance().getCurrentUser();
   	DatabaseReference databaseReferenceDislike = FirebaseDatabase.getInstance().getReference().child("Dislikes").child(postid);
   	databaseReferenceDislike.addValueEventListener(new ValueEventListener() {
       	@Override
       	public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               if(dataSnapshot.child(firebaseUser.getUid()).exists()){
               	ImageViewCompat.setImageTintList(imageView, ColorStateList.valueOf(colored));
                   imageView.setTag("disliked");
                   enabledView.setEnabled(false);
           	}else{
               	ImageViewCompat.setImageTintList(imageView, ColorStateList.valueOf(color));
                   imageView.setTag("dislike");
                   enabledView.setEnabled(true);
           	}
       	}
 
       	@Override
       	public void onCancelled(@NonNull DatabaseError databaseError) {
 
       	}
   	});
   }
  
   private void  isShop(String postid,ImageView imageView,ImageView enabledView){
       FirebaseUser firebaseUser =  FirebaseAuth.getInstance().getCurrentUser();
   	DatabaseReference databaseReferenceDislike = FirebaseDatabase.getInstance().getReference().child("Shops").child(postid);
       databaseReferenceDislike.addValueEventListener(new ValueEventListener() {
       	@Override
       	public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               if(dataSnapshot.child(firebaseUser.getUid()).exists()){
               	ImageViewCompat.setImageTintList(imageView, ColorStateList.valueOf(colored));
                   imageView.setTag("shoped");
                   enabledView.setEnabled(false);
           	}else{
               	ImageViewCompat.setImageTintList(imageView, ColorStateList.valueOf(color));
                   imageView.setTag("shop");
                   enabledView.setEnabled(true);
           	}
       	}
 
       	@Override
       	public void onCancelled(@NonNull DatabaseError databaseError) {
 
       	}
   	});
   }
 
   private void  isDontShop(String postid,ImageView imageView,ImageView enabledView){
   	FirebaseUser firebaseUser =  FirebaseAuth.getInstance().getCurrentUser();
   	DatabaseReference databaseReferenceDislike = FirebaseDatabase.getInstance().getReference().child("Dontshops").child(postid);
       databaseReferenceDislike.addValueEventListener(new ValueEventListener() {
       	@Override
       	public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
           	if(dataSnapshot.child(firebaseUser.getUid()).exists()){
               	ImageViewCompat.setImageTintList(imageView, ColorStateList.valueOf(colored));
                   imageView.setTag("dontshoped");
                   enabledView.setEnabled(false);
           	}else{
    	           ImageViewCompat.setImageTintList(imageView, ColorStateList.valueOf(color));
                   imageView.setTag("dontshop");
                   enabledView.setEnabled(true);
           	}
       	}
 
       	@Override
       	public void onCancelled(@NonNull DatabaseError databaseError) {
 
       	}
   	});
   }
   private void  isStart(String postid,ImageView imageView,BarChart barChart){
   	FirebaseUser firebaseUser =  FirebaseAuth.getInstance().getCurrentUser();
   	DatabaseReference databaseReferenceDislike = FirebaseDatabase.getInstance().getReference().child("Starts").child(postid);
       databaseReferenceDislike.addValueEventListener(new ValueEventListener() {
       	@Override
       	public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
           	int isyeri = 0,gece =0,gunluk=0,diger=0;
           	List<String> key = new ArrayList<>();
               if(dataSnapshot.child(firebaseUser.getUid()).exists()){
               	ImageViewCompat.setImageTintList(imageView, ColorStateList.valueOf(colored));
               	for (DataSnapshot  dataSnapshot1 : dataSnapshot.getChildren()){
                    	key.add((String) dataSnapshot1.getValue());
                       if(dataSnapshot1.getKey().equals(firebaseUser.getUid())){
              	         starText = (String)dataSnapshot1.getValue();
                           imageView.setTag("started");
                   	}
               	}
               	for (String s: key){
                       if(s.equals("İşyeri")){
                       	isyeri++;
                   	}else if(s.equals("Gece")){
                       	gece++;
                   	}else if(s.equals("Günlük")){
                       	gunluk++;
                   	}else{
                       	diger++;
                   	}
               	}
               	twoDataList.add(new TwoData("İşyeri",isyeri));
               	twoDataList.add(new TwoData("Gece",gece));
        	       twoDataList.add(new TwoData("Günlük",gunluk));
               	twoDataList.add(new TwoData("Diğer",diger));
               	if(graphics){
                       graphics(twoDataList,"Kıyafet Seçimi","Grafik",barChart);
               	}
               }else{
                   imageView.setTag("start");
           	}
       	}
 
       	@Override
       	public void onCancelled(@NonNull DatabaseError databaseError) {
 
       	}
   	});
   }
 
   private  void nrLikes(final TextView likeText,final TextView dontLikeText,String postid,String tagTxt,String dontTagTxt,String databaseTag,String databaseDontTag,BarChart barChart){
   	twoDataList = new ArrayList<>();
   	DatabaseReference databaseReference =  FirebaseDatabase.getInstance().getReference().child(databaseTag).child(postid);
       databaseReference.addValueEventListener(new ValueEventListener() {
       	@Override
       	public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              likeText.setText(dataSnapshot.getChildrenCount() + tagTxt+" ");
          	twoDataList.add(new TwoData(tagTxt,(int)dataSnapshot.getChildrenCount()));
       	}
 
       	@Override
       	public void onCancelled(@NonNull DatabaseError databaseError) {
 
       	}
   	});
   	DatabaseReference  databaseReference1 = FirebaseDatabase.getInstance().getReference().child(databaseDontTag).child(postid);
       databaseReference1.addValueEventListener(new ValueEventListener() {
       	@Override
       	public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               dontLikeText.setText(dataSnapshot.getChildrenCount() + dontTagTxt+" ");
           	twoDataList.add(new TwoData(dontTagTxt,(int)dataSnapshot.getChildrenCount()));
           	if(graphics){
                   graphics(twoDataList,tagTxt+" ve " +dontTagTxt+"Oranı","Grafik",barChart);
     	      }
       	}
       	@Override
       	public void onCancelled(@NonNull DatabaseError databaseError) {
 
       	}
   	});
 
   }
 
   private void publisherInfo(ImageView imageProfil,TextView username,TextView publisher,String userid ){
   	DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
   	reference.addValueEventListener(new ValueEventListener() {
       	@Override
       	public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
           	User user = dataSnapshot.getValue(User.class);
           	Glide.with(context).load(user.getImageurl()).into(imageProfil);
               username.setText(user.getUsername());
               publisher.setText(user.getUsername());
       	}
 
       	@Override
       	public void onCancelled(@NonNull DatabaseError databaseError) {
 
       	}
   	});
   }
 
   private View setChooseAlertDialog(String star,String postId,ImageView imageView){
   	View chooseView =  LayoutInflater.from(context).inflate(R.layout.alert_dialog_start,null);
   	LinearLayout answerLayout = chooseView.findViewById(R.id.answerLayout);
   	TextView textView = chooseView.findViewById(R.id.textView);
   	TextView chooseText = chooseView.findViewById(R.id.chooseText);
   	LinearLayout radioLayout = chooseView.findViewById(R.id.radioLayout);
   	RadioGroup  radioGroup = chooseView.findViewById(R.id.radioGroup);
   	RadioButton  isyeri = chooseView.findViewById(R.id.isyeri);
   	RadioButton gece = chooseView.findViewById(R.id.gece);
   	RadioButton gunluk = chooseView.findViewById(R.id.gunluk);
   	RadioButton diger = chooseView.findViewById(R.id.diger);
   	Button vazgec = chooseView.findViewById(R.id.cancelBtn);
   	Button tamam = chooseView.findViewById(R.id.okBtn);
       if(imageView.getTag().equals("started")){
       	answerLayout.setVisibility(View.VISIBLE);
       	radioLayout.setVisibility(View.GONE);
       	textView.setVisibility(View.GONE);
       	chooseText.setText("Oylamaya verilen cevap :" + star);
       	tamam.setText("Tamam");
   	}else {
       	answerLayout.setVisibility(View.GONE);
       	radioLayout.setVisibility(View.VISIBLE);
       	textView.setVisibility(View.VISIBLE);
       	tamam.setText("Ekle");
           radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
           	@Override
           	public void onCheckedChanged(RadioGroup group, int checkedId) {
           	    if(checkedId ==isyeri.getId()){
                   	starText ="İşyeri";
               	}else if(checkedId ==gece.getId()){
                   	starText = "Gece";
               	}else if(checkedId == gunluk.getId()){
                  	 starText = "Günlük";
               	}else {
                   	starText = "Diğer";
               	}
           	}
       	});
       	FirebaseDatabase.getInstance().getReference().child("Starts").child(postId).child(firebaseUser.getUid()).setValue(starText);
   	}
 
   	vazgec.setOnClickListener(v -> dialogStart.dismiss());
   	tamam.setOnClickListener(v -> {
       	dialogStart.dismiss();
 
   	});
   	return chooseView;
   }
   private  void addNotification(String userid,String postid,String message){
   	DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Notifications").child(userid);
   	HashMap<String,Object> hashMap = new HashMap<>();
       hashMap.put("userid",firebaseUser.getUid());
   	hashMap.put("text",message);
   	hashMap.put("postid",postid);
   	hashMap.put("ispost",true);
   	databaseReference.push().setValue(hashMap);
   }
}
 
User Adapter
 
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
 
   private Context context;
   private List<User> userList;
   private FirebaseUser firebaseUser;
 
   public UserAdapter(Context context, List<User> userList) {
   	this.context = context;
   	this.userList = userList;
   }
 
   @NonNull
   @Override
   public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
   	View view = LayoutInflater.from(context).inflate(R.layout.user_item,parent,false);
   	return new UserAdapter.ViewHolder(view);
   }
   @Override
   public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
  	firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
  	final User user =  userList.get(position);
  	holder.btnItemFollow.setVisibility(View.VISIBLE);
      holder.itemusername.setText(user.getUsername());
      holder.fullName.setText(user.getFullname());
  	Glide.with(context).load(user.getImageurl()).into(holder.imageView);
      isFollowing(user.getId(),holder.btnItemFollow);
      if(user.getId().equals(firebaseUser.getUid())){
          holder.btnItemFollow.setVisibility(View.GONE);
  	}
 
  	holder.itemView.setOnClickListener(new View.OnClickListener() {
      	@Override
      	public void onClick(View v) {
          	PreferencesUtil.getInstance(context).putKeyValue(PreferencesUtil.PROFILE_ID,user.getId());
          	((FragmentActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,new UserFragment()).commit();
      	}
  	});
 
      holder.btnItemFollow.setOnClickListener(new View.OnClickListener() {
      	@Override
      	public void onClick(View v) {
              if(holder.btnItemFollow.getText().toString().equals("follow")){
              	FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid()).child("following").child(user.getId()).setValue(true);
              	FirebaseDatabase.getInstance().getReference().child("Follow").child(user.getId()).child("followers").child(firebaseUser.getUid()).setValue(true);
              	addNotification(user.getId(),"started following you");
          	}else{
              	FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid()).child("following").child(user.getId()).removeValue();
              	FirebaseDatabase.getInstance().getReference().child("Follow").child(user.getId()).child("followers").child(firebaseUser.getUid()).removeValue();
          	}
      	}
  	});
 
   }
 
   @Override
   public int getItemCount() {
   	return userList.size();
   }
 
   public class ViewHolder extends RecyclerView.ViewHolder{
 
   	private TextView itemusername,fullName;
   	private CircleImageView imageView;
   	private Button btnItemFollow;
   	public ViewHolder(@NonNull View itemView) {
       	super(itemView);
           itemusername = itemView.findViewById(R.id.userItemName);
       	fullName = itemView.findViewById(R.id.fullItemName);
       	imageView = itemView.findViewById(R.id.imageProfile);
       	btnItemFollow = itemView.findViewById(R.id.btnItemFollow);
   	}
   }
 
   private  void addNotification(String userid,String message){
   	DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Notifications").child(userid);
   	HashMap<String,Object> hashMap = new HashMap<>();
       hashMap.put("userid",firebaseUser.getUid());
   	hashMap.put("text",message);
       hashMap.put("postid","");
   	hashMap.put("ispost",false);
       databaseReference.push().setValue(hashMap);
   }
   private void isFollowing(String userid,Button button){
   	DatabaseReference  reference = FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid()).child("following");
   	reference.addValueEventListener(new ValueEventListener() {
       	@Override
       	public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               if(dataSnapshot.child(userid).exists()){
               	button.setText("following");
           	}else{
                   button.setText("follow");
           	}
       	}
 
       	@Override
       	public void onCancelled(@NonNull DatabaseError databaseError) {
 
       	}
   	});
   }
}
 
 
public class HomeFragment extends Fragment {
   private RecyclerView recyclerView;
   private PostAdapter postAdapter;
   private List<Post> postLists;
   private ProgressBar progressBar;
   private List<String> followingList;
   @Nullable
   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
   	View view = inflater.inflate(R.layout.home_fragment,container,false);
   	recyclerView = view.findViewById(R.id.recyclerView);
   	recyclerView.setHasFixedSize(true);
   	LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
 	  linearLayoutManager.setReverseLayout(true);
       linearLayoutManager.setStackFromEnd(true);
       recyclerView.setLayoutManager(linearLayoutManager);
   	postLists = new ArrayList<>();
   	postAdapter = new PostAdapter(getContext(),postLists,false);
   	recyclerView.setAdapter(postAdapter);
   	progressBar = view.findViewById(R.id.progressBar);
   	checkFollowing();
   	return view;
   }
   private void checkFollowing(){
   	progressBar.setVisibility(View.VISIBLE);
   	followingList = new ArrayList<>();
   	DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Follow")
           	.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
           	.child("following");
       databaseReference.addValueEventListener(new ValueEventListener() {
       	@Override
       	public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
           	followingList.clear();
           	for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                   followingList.add(snapshot.getKey());
           	}
           	readPosts();
       	}
 
       	@Override
       	public void onCancelled(@NonNull DatabaseError databaseError) {
           	progressBar.setVisibility(View.GONE);
       	}
   	});
 
   }
   private void readPosts(){
   	DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Posts");
       databaseReference.addValueEventListener(new ValueEventListener() {
       	@Override
       	public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
           	postLists.clear();
           	for(DataSnapshot snapshot : dataSnapshot.getChildren()){
               	Post post = snapshot.getValue(Post.class);
               	for (String id: followingList){
                       if(post.getPublisher().equals(id)){
                       	postLists.add(post);
                   	}
               	}
           	}
       	    postAdapter.notifyDataSetChanged();
           	progressBar.setVisibility(View.GONE);
       	}
 
       	@Override
       	public void onCancelled(@NonNull DatabaseError databaseError) {
           	progressBar.setVisibility(View.GONE);
 
       	}
   	});
   }
}
 
 
 
public class NotificationFragment extends Fragment {
 
   private RecyclerView recyclerView;
   private NotificationAdapter notificationAdapter;
 
   private List<Notifications> notificationsList;
   @Nullable
   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
 
   	View view = inflater.inflate(R.layout.notification_fragment,container,false);
   	recyclerView = view.findViewById(R.id.recyclerView);
   	recyclerView.setHasFixedSize(true);
   	LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
       recyclerView.setLayoutManager(linearLayoutManager);
   	notificationsList =  new ArrayList<>();
   	notificationAdapter = new NotificationAdapter(getContext(),notificationsList);
       recyclerView.setAdapter(notificationAdapter);
   	readNotification();
   	return view;
   }
 
   private void readNotification() {
   	FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
   	DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Notifications").child(firebaseUser.getUid());
       databaseReference.addValueEventListener(new ValueEventListener() {
       	@Override
       	public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               notificationsList.clear();
           	for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
               	Notifications notifications = dataSnapshot1.getValue(Notifications.class);
               	notificationsList.add(notifications);
           	}
           	Collections.reverse(notificationsList);
               notificationAdapter.notifyDataSetChanged();
       	}
 
       	@Override
       	public void onCancelled(@NonNull DatabaseError databaseError) {
 
           }
   	});
 
   }
}
 
 
public class SearchFragment extends Fragment {
   private RecyclerView recyclerView;
   private UserAdapter  userAdapter;
   private List<User> mUser;
   EditText searchBar;
   ProgressBar progressBar;
   @Nullable
   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
 
   	View view = inflater.inflate(R.layout.search_fragment,container,false);
   	recyclerView = view.findViewById(R.id.recyclerView);
   	recyclerView.setHasFixedSize(true);
   	recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
   	searchBar = view.findViewById(R.id.searchBar);
   	progressBar = view.findViewById(R.id.progressBar);
   	mUser = new ArrayList<>();
   	userAdapter = new UserAdapter(getContext(),mUser);
   	recyclerView.setAdapter(userAdapter);
   	readUser();
 
   	searchBar.addTextChangedListener(new TextWatcher() {
       	@Override
       	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
 
       	}
 
       	@Override
       	public void onTextChanged(CharSequence s, int start, int before, int count) {
           	searchUsers(s.toString().toLowerCase());
       	}
 
       	@Override
       	public void afterTextChanged(Editable s) {
 
       	}
   	});
   	return view;
   }
 
   private void searchUsers(String name){
   	progressBar.setVisibility(View.VISIBLE);
   	Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("username").startAt(name).endAt(name+"\uf8ff");
   	query.addValueEventListener(new ValueEventListener() {
       	@Override
       	public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
           	mUser.clear();
           	for (DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){
               	User user = dataSnapshot1.getValue(User.class);
               	mUser.add(user);
           	}
               userAdapter.notifyDataSetChanged();
       	}
 
       	@Override
       	public void onCancelled(@NonNull DatabaseError databaseError) {
 
       	}
   	});
   	progressBar.setVisibility(View.GONE);
   }
 
  
   private  void readUser(){
   	progressBar.setVisibility(View.VISIBLE);
   	DatabaseReference databaseReference =  FirebaseDatabase.getInstance().getReference("Users");
       databaseReference.addValueEventListener(new ValueEventListener() {
       	@Override
       	public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               if(searchBar.getText().toString().isEmpty()){
               	mUser.clear();
               	for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
 	                  User user = dataSnapshot1.getValue(User.class);
                   	mUser.add(user);
               	}
                   userAdapter.notifyDataSetChanged();
           	}
       	}
 
       	@Override
       	public void onCancelled(@NonNull DatabaseError databaseError) {
 
       	}
   	});
   	progressBar.setVisibility(View.GONE);
   }
}
 
 
 
public class UserFragment extends Fragment {
	ImageView imageProfile,options;
	TextView posts,followers,following,fullname,bio,userName;
	Button editProfile,myFashions,opinions;
	RecyclerView recyclerView;
	PhotoAdapter photoAdapter;
	List<Post> postList;
	FirebaseUser firebaseUser;
	String profileId;
 
	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    	View view = inflater.inflate(R.layout.user_fragment,container,false);
    	firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    	profileId = PreferencesUtil.getInstance(getContext()).getString(PreferencesUtil.PROFILE_ID,"none");
 
    	imageProfile = view.findViewById(R.id.imageProfile);
    	options = view.findViewById(R.id.options);
    	posts = view.findViewById(R.id.userPost);
    	followers = view.findViewById(R.id.userFollowers);
    	following = view.findViewById(R.id.userFollowing);
    	fullname = view.findViewById(R.id.userFullName);
    	bio = view.findViewById(R.id.userBio);
    	userName = view.findViewById(R.id.userName);
    	editProfile = view.findViewById(R.id.editProfile);
    	myFashions = view.findViewById(R.id.myFashion);
    	opinions = view.findViewById(R.id.opinions);
    	recyclerView = view.findViewById(R.id.recyclerViewFashion);
    	recyclerView.setHasFixedSize(true);
    	LinearLayoutManager linearLayoutManager = new GridLayoutManager(getContext(),3);
        recyclerView.setLayoutManager(linearLayoutManager);
    	postList = new ArrayList<>();
    	photoAdapter = new PhotoAdapter(getContext(),postList);
    	recyclerView.setAdapter(photoAdapter);
    	userInfo();
    	getFollowers();
    	getPosts();
    	myFotos("myfashion");
 
    	options.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View v) {
      	      Intent intent = new Intent(getContext(), OptionsActivity.class);
            	startActivity(intent);
        	}
    	});
 
    	myFashions.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View v) {
            	myFotos("myfashion");
        	}
    	});
 
    	opinions.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View v) {
            	myFotos("opinions");
        	}
    	});
 
 
        if(profileId.equals(firebaseUser.getUid())){
        	editProfile.setText("Edit Profile");
    	}else{
        	checkFollow();
 
    	}
 
    	editProfile.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View v) {
   	         String btn = editProfile.getText().toString();
            	if(btn.equals("Edit Profile")){
                	startActivity(new Intent(getContext(), EditProfileActivity.class));
            	}else if(btn.equals("follow")){
            	    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid()).child("following").child(profileId).setValue(true);
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(profileId).child("followers").child(firebaseUser.getUid()).setValue(true);
                	addNotification();
            	}else if(btn.equals("following")){
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid()).child("following").child(profileId).removeValue();
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(profileId).child("followers").child(firebaseUser.getUid()).removeValue();
            	}
        	}
    	});
    	return view;
	}
 
	private  void addNotification(){
    	DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Notifications").child(profileId);
    	HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("userid",firebaseUser.getUid());
        hashMap.put("text","started following you");
        hashMap.put("postid","");
    	hashMap.put("ispost",false);
    	databaseReference.push().setValue(hashMap);
	}
 
	private  void userInfo(){
    	DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(profileId);
        databaseReference.addValueEventListener(new ValueEventListener() {
        	@Override
        	public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            	if (getContext() ==  null){
                	return;
  	          }
            	User user = dataSnapshot.getValue(User.class);
                Glide.with(getContext()).load(user.getImageurl()).into(imageProfile);
                userName.setText(user.getUsername());
            	fullname.setText(user.getFullname());
            	bio.setText(user.getBio());
        	}
 
        	@Override
        	public void onCancelled(@NonNull DatabaseError databaseError) {
 
        	}
    	});
	}
 
 
	private void checkFollow(){
    	DatabaseReference reference =  FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid()).child("following");
    	reference.addValueEventListener(new ValueEventListener() {
        	@Override
        	public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               if(dataSnapshot.child(profileId).exists()){
                   editProfile.setText("following");
           	}else{
                   editProfile.setText("follow");
           	}
        	}
 
        	@Override
        	public void onCancelled(@NonNull DatabaseError databaseError) {
 
        	}
    	});
	}
 
	private void getFollowers(){
    	DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Follow").child(profileId).child("followers");
        databaseReference.addValueEventListener(new ValueEventListener() {
        	@Override
        	public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               	followers.setText(String.valueOf(dataSnapshot.getChildrenCount()));
        	}
 
        	@Override
        	public void onCancelled(@NonNull DatabaseError databaseError) {
 
        	}
    	});
    	DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("Follow").child(profileId).child("following");
        databaseReference1.addValueEventListener(new ValueEventListener() {
        	@Override
        	public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
	            following.setText(String.valueOf(dataSnapshot.getChildrenCount()));
        	}
 
        	@Override
        	public void onCancelled(@NonNull DatabaseError databaseError) {
 
        	}
    	});
	}
 
	private void getPosts(){
    	DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Posts");
  	  databaseReference.addValueEventListener(new ValueEventListener() {
        	@Override
        	public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            	int i = 0;
            	for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                	Post post = dataSnapshot1.getValue(Post.class);
                    if(post.getPublisher().equals(profileId)){
                    	i++;
                	}
            	}
            	posts.setText(""+i);
            }
 
        	@Override
        	public void onCancelled(@NonNull DatabaseError databaseError) {
 
        	}
    	});
	}
 
	private void myFotos(String type){
    	DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Posts");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
        	public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            	postList.clear();
            	for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                	Post post = dataSnapshot1.getValue(Post.class);
                    if(post.getPublisher().equals(profileId) && type.equals(post.getCategory())){
                    	postList.add(post);
                	}
            	}
            	Collections.reverse(postList);
     	       photoAdapter.notifyDataSetChanged();
        	}
 
        	@Override
        	public void onCancelled(@NonNull DatabaseError databaseError) {
 
        	}
    	});
	}
}
 
 
public class Comments {
   private String comment;
   private String publisher;
   private String time;
 
   public Comments(String comment, String publisher, String time) {
   	this.comment = comment;
   	this.publisher = publisher;
   	this.time = time;
   }
 
   public Comments() {
   }
 
   public String getComment() {
   	return comment;
   }
 
   public void setComment(String comment) {
   	this.comment = comment;
   }
 
   public String getPublisher() {
   	return publisher;
   }
 
   public void setPublisher(String publisher) {
   	this.publisher = publisher;
   }
 
   public String getTime() {
   	return time;
   }
 
   public void setTime(String time) {
   	this.time = time;
   }
 
}
 
 
public class Notifications {
 
   private String userid;
   private String text;
   private String postid;
   private boolean ispost;
 
   public Notifications(String userid, String text, String postid, boolean ispost) {
   	this.userid = userid;
   	this.text = text;
   	this.postid = postid;
 	  this.ispost = ispost;
   }
 
   public Notifications() {
   }
 
   public String getUserid() {
   	return userid;
   }
 
   public void setUserid(String userid) {
   	this.userid = userid;
   }
 
   public String getText() {
   	return text;
   }
 
   public void setText(String text) {
   	this.text = text;
   }
 
   public String getPostid() {
   	return postid;
   }
 
   public void setPostid(String postid) {
   	this.postid = postid;
   }
 
   public boolean isIspost() {
   	return ispost;
   }
 
   public void setIspost(boolean ispost) {
   	this.ispost = ispost;
   }
}
 
public class Post {
   private String postid;
   private String postimage;
   private String description;
   private String category;
   private String publisher;
 
 
   public Post(String postid, String postimage, String description, String category, String publisher) {
   	this.postid = postid;
   	this.postimage = postimage;
   	this.description = description;
   	this.category = category;
   	this.publisher = publisher;
   }
 
   public Post() {
   }
 
   public String getPostid() {
   	return postid;
   }
 
   public void setPostid(String postid) {
   	this.postid = postid;
   }
 
   public String getPostimage() {
   	return postimage;
   }
 
   public void setPostimage(String postimage) {
   	this.postimage = postimage;
   }
 
   public String getDescription() {
   	return description;
   }
 
   public void setDescription(String description) {
   	this.description = description;
   }
 
   public String getCategory() {
   	return category;
   }
 
   public void setCategory(String category) {
   	this.category = category;
   }
 
   public String getPublisher() {
   	return publisher;
   }
 
   public void setPublisher(String publisher) {
   	this.publisher = publisher;
   }
}
 
 
 
public class TwoData {
   private String name;
   private int number;
 
   public TwoData(String name, int number) {
   	this.name = name;
   	this.number = number;
   }
 
   public String getName() {
   	return name;
   }
 
   public void setName(String name) {
   	this.name = name;
   }
 
   public int getNumber() {
   	return number;
   }
 
   public void setNumber(int number) {
   	this.number = number;
   }
}
 
public class User {
   private String id;
   private String username;
   private String fullname;
   private String imageurl;
   private String bio;
   private String phone;
   private String gender;
   private String old;
 
   public User(String id, String username, String fullname, String imageurl, String bio, String phone, String gender,String old) {
   	this.id = id;
   	this.username = username;
   	this.fullname = fullname;
   	this.imageurl = imageurl;
   	this.bio = bio;
   	this.phone = phone;
   	this.gender = gender;
   	this.old =old;
   }
 
   public String getOld() {
   	return old;
   }
 
   public void setOld(String old) {
   	this.old = old;
   }
 
   public User() {
   }
 
   public String getId() {
   	return id;
   }
 
   public void setId(String id) {
   	this.id = id;
   }
 
   public String getUsername() {
   	return username;
   }
 
   public void setUsername(String username) {
   	this.username = username;
   }
 
   public String getFullname() {
   	return fullname;
   }
 
   public void setFullname(String fullname) {
   	this.fullname = fullname;
   }
 
   public String getImageurl() {
   	return imageurl;
   }
 
   public void setImageurl(String imageurl) {
   	this.imageurl = imageurl;
   }
 
   public String getBio() {
   	return bio;
   }
 
   public void setBio(String bio) {
   	this.bio = bio;
   }
 
   public String getPhone() {
   	return phone;
   }
 
   public void setPhone(String phone) {
   	this.phone = phone;
   }
 
   public String getGender() {
   	return gender;
   }
 
   public void setGender(String gender) {
   	this.gender = gender;
   }
}
 
 
 
public class FasherUtil {
 
   public static String getTwoCharFormat(long v) {
   	return (v > 0 && v < 10) ? ("0" + v) : (v + "");
   }
 
 
   public static Date parseDatetime(String datetime, String format) {
   	SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.getDefault());
   	try {
       	return formatter.parse(datetime);
   	} catch (ParseException e) {
       	return null;
   	}
   }
 
   public static boolean isNullOrEmpty(String param) {
   	return param == null || param.trim().isEmpty();
   }
 
   public static <T> boolean isNullOrEmpty(List<T> param) {
   	return param == null || param.isEmpty();
   }
 
   public static String convertBitmapToBase64(Bitmap photo) {
   	ByteArrayOutputStream baos = new ByteArrayOutputStream();
   	photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
   	byte[] imageBytes = baos.toByteArray();
   	return Base64.encodeToString(imageBytes, Base64.DEFAULT);
   }
 
 
 
   public static String stringJoin(String delimeter, List<String> listValues) {
   	StringBuilder sb = new StringBuilder();
   	for (int i = 0; i < listValues.size(); i++) {
       	String v = listValues.get(i);
       	if (!isNullOrEmpty(v)) {
           	sb.append(v);
           	if (i != listValues.size() - 1) {
           	    sb.append(delimeter);
           	}
       	}
   	}
   	return sb.toString();
   }
 
 
}
 
 
public class PreferencesUtil {
 
   private static String PREFERENCE_FILE_KEY = "FasherPreferences";
   public static String PROFILE_ID="profileID";
 
   private static PreferencesUtil instance = new PreferencesUtil();
   private Context context;
 
   private PreferencesUtil(){}
 
   public static PreferencesUtil getInstance(Context context){
   	instance.context = context;
   	return instance;
   }
 
   public void putKeyValue(String key, String value){
   	SharedPreferences.Editor editor = getPreferences().edit();
   	editor.putString(key, value);
   	editor.apply();
   }
 
   public void putKeyValue(String key, Integer value){
   	SharedPreferences.Editor editor = getPreferences().edit();
   	editor.putInt(key, value);
   	editor.apply();
   }
   public void putKeyValue(String key, boolean value){
	   SharedPreferences.Editor editor = getPreferences().edit();
   	editor.putBoolean(key, value);
   	editor.apply();
   }
 
   public String getString(String key, String def){
   	return getPreferences().getString(key, def);
   }
 
   public Integer getInt(String key, Integer def){
   	return getPreferences().getInt(key, def);
   }
   public Boolean getBoolean(String key, Boolean def){
   	return getPreferences().getBoolean(key, def);
   }
 
   private SharedPreferences getPreferences(){
   	return context.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
   }
 
   public void clearAll(){
   	SharedPreferences.Editor editor = getPreferences().edit();
   	editor.clear();
   	editor.apply();
   	editor.commit();
   }
}
 
 
 
public class AddPhoto extends AppCompatActivity {
   Uri imageUri;
   String myUrl = "";
   StorageTask uploadTask;
   StorageReference storageReference;
 
   ImageView closeImage,imageAdd;
   TextView post;
   EditText description;
   ToggleButton toggleButton;
   ProgressBar progressBar;
   private String category="opinions";
 
   @Override
   protected void onCreate(Bundle savedInstanceState) {
   	super.onCreate(savedInstanceState);
   	setContentView(R.layout.add_photo_activity);
   	closeImage = findViewById(R.id.closeImageView);
   	post = findViewById(R.id.postTxt);
   	imageAdd = findViewById(R.id.imageAdd);
   	description = findViewById(R.id.descriptionEdt);
   	toggleButton = findViewById(R.id.toggleMod);
   	progressBar = findViewById(R.id.progressBar);
   	storageReference = FirebaseStorage.getInstance().getReference("Posts");
 
   	closeImage.setOnClickListener(new View.OnClickListener() {
       	@Override
       	public void onClick(View v) {
           	startActivity(new Intent(AddPhoto.this,HomeActivity.class));
           	finish();
       	}
   	});
 
   	post.setOnClickListener(new View.OnClickListener() {
       	@Override
       	public void onClick(View v) {
           	uploadImage();
       	}
   	});
 
   	toggleButton.setOnClickListener(new View.OnClickListener() {
       	@Override
       	public void onClick(View v) {
           	if(toggleButton.isChecked()){
               	category = "myfashion";
           	}else{
               	category = "opinions";
               }
       	}
   	});
   	CropImage.activity().setAspectRatio(1,1).start(AddPhoto.this);
   }
 
 
   private String getFileExtensions(Uri uri){
   	ContentResolver contentResolver = getContentResolver();
   	MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
   	return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
   }
 
   @Override
   protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
   	super.onActivityResult(requestCode, resultCode, data);
 
   	if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
       	CropImage.ActivityResult result =  CropImage.getActivityResult(data);
       	imageUri = result.getUri();
       	imageAdd.setImageURI(imageUri);
   	}else{
       	Toast.makeText(this, R.string.something_gone_wrong,Toast.LENGTH_SHORT).show();
   	    startActivity(new Intent(AddPhoto.this,HomeActivity.class));
       	finish();
   	}
   }
 
 
   private void uploadImage() {
   	progressBar.setVisibility(View.VISIBLE);
   	if(imageUri != null){
       	StorageReference fileReferences = storageReference.child(System.currentTimeMillis()+"."+getFileExtensions(imageUri));
       	uploadTask = fileReferences.putFile(imageUri);
       	uploadTask.continueWithTask(new Continuation() {
           	@Override
           	public Object then(@NonNull Task task) throws Exception {
               	if(!task.isSuccessful()){
                   	throw task.getException();
               	}
               	return fileReferences.getDownloadUrl();
           	}
       	}).addOnCompleteListener(new OnCompleteListener<Uri>() {
           	@Override
           	public void onComplete(@NonNull Task<Uri> task) {
               	if(task.isSuccessful()){
                  	Uri downloadUri = task.getResult();
                  	myUrl =  downloadUri.toString();
                   	DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Posts");
                   	String postId =  databaseReference.push().getKey();
                       HashMap<String,Object> hashMap=  new HashMap<>();
                       hashMap.put("postid",postId);
               	    hashMap.put("postimage",myUrl);
                       hashMap.put("description",description.getText().toString());
                       hashMap.put("category",category);
                       hashMap.put("publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());
 
                       databaseReference.child(postId).setValue(hashMap);
                       progressBar.setVisibility(View.GONE);
                   	startActivity(new Intent(AddPhoto.this,HomeActivity.class));
        	           finish();
               	}else{
                   	Toast.makeText(getApplicationContext(),"Image Addded Error!",Toast.LENGTH_SHORT).show();
               	}
           	}
       	}).addOnFailureListener(new OnFailureListener() {
           	@Override
           	public void onFailure(@NonNull Exception e) {
               	Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
           	}
       	});
   	}else{
       	Toast.makeText(getApplicationContext(), R.string.no_image_selected,Toast.LENGTH_SHORT).show();
   	}
   }
}
 
 
 
public class CommentsActivity extends AppCompatActivity {
 
   EditText addComments;
   ImageView sendComments,imageProfile;
   String postid;
   String publisherid;
   RecyclerView recyclerView;
   CommentAdapter commentAdapter;
   FirebaseUser firebaseUser ;
   List<Comments> commentsList;
   @Override
   protected void onCreate(Bundle savedInstanceState) {
   	super.onCreate(savedInstanceState);
   	setContentView(R.layout.activity_comment);
   	Toolbar toolbar = findViewById(R.id.toolbar);
   	setSupportActionBar(toolbar);
   	int color = Color.rgb(0,0,0);
   	toolbar.setTitleTextColor(color);
       getSupportActionBar().setTitle("Comments");
       getSupportActionBar().setDisplayHomeAsUpEnabled(true);
   	toolbar.setNavigationOnClickListener(new View.OnClickListener() {
       	@Override
       	public void onClick(View v) {
           	finish();
       	}
   	});
 
   	addComments = findViewById(R.id.addCommentEdt);
   	sendComments = findViewById(R.id.sendComment);
   	imageProfile = findViewById(R.id.imageProfile);
   	recyclerView = findViewById(R.id.recyclerView);
   	recyclerView.setHasFixedSize(true);
   	LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CommentsActivity.this,LinearLayoutManager.VERTICAL,false);
       recyclerView.setLayoutManager(linearLayoutManager);
   	commentsList = new ArrayList<>();
   	commentAdapter = new CommentAdapter(CommentsActivity.this,commentsList);
   	recyclerView.setAdapter(commentAdapter);
 
   	firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
   	Intent intent = getIntent();
   	postid = intent.getStringExtra("postid");
   	publisherid = intent.getStringExtra("publisherid");
   	getImage();
       readComments(postid);
   	sendComments.setOnClickListener(new View.OnClickListener() {
       	@Override
       	public void onClick(View v) {
               if(addComments.getText().toString().equals("")){
               	Toast.makeText(CommentsActivity.this,"Empty comment dont send.",Toast.LENGTH_SHORT).show();
           	}else{
               	addComment();
           	}
       	}
   	});
 
   }
 
   private void addComment() {
   	SimpleDateFormat formatter= new SimpleDateFormat("yyyy.MM.dd HH:mm");
   	Date date = new Date(System.currentTimeMillis());
   	DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Comments").child(postid);
   	HashMap<String,Object> hashMap = new HashMap<>();
       hashMap.put("comment",addComments.getText().toString().trim());
       hashMap.put("publisher",firebaseUser.getUid());
   	hashMap.put("time",formatter.format(date));
       databaseReference.push().setValue(hashMap);
   	addNotification();
   	addComments.setText("");
   }
 
   private  void addNotification(){
   	DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Notifications").child(publisherid);
   	HashMap<String,Object> hashMap = new HashMap<>();
       hashMap.put("userid",firebaseUser.getUid());
   	hashMap.put("text","commented:"+addComments.getText().toString());
   	hashMap.put("postid",postid);
   	hashMap.put("ispost",true);
       databaseReference.push().setValue(hashMap);
   }
 
   private void getImage(){
   	DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
       databaseReference.addValueEventListener(new ValueEventListener() {
     	  @Override
       	public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
           	User user = dataSnapshot.getValue(User.class);
           	Glide.with(getApplicationContext()).load(user.getImageurl()).into(imageProfile);
       	}
 
       	@Override
       	public void onCancelled(@NonNull DatabaseError databaseError) {
 
       	}
   	});
   }
 
   private void readComments(String postid){
       DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Comments").child(postid);
       databaseReference.addValueEventListener(new ValueEventListener() {
       	@Override
       	public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
           	commentsList.clear();
           	for (DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){
               	Comments comments1 =  dataSnapshot1.getValue(Comments.class);
               	commentsList.add(comments1);
           	}
               commentAdapter.notifyDataSetChanged();
       	}
 
       	@Override
       	public void onCancelled(@NonNull DatabaseError databaseError) {
 
       	}
   	});
 
   }
}
 
public class EditProfileActivity extends AppCompatActivity {
   private ImageView close,imageProfile;
   private TextView save,imageProfileChange;
   private MaterialEditText fullName,userName,bio;
   FirebaseUser firebaseUser;
   ProgressBar progressBar;
   private Uri imageUri;
   private StorageTask uploadTask;
   StorageReference storageReference;
  
   @Override
   protected void onCreate(Bundle savedInstanceState) {
   	super.onCreate(savedInstanceState);
   	setContentView(R.layout.activity_edit_profile);
   	close = findViewById(R.id.close);
   	save = findViewById(R.id.save);
   	imageProfile = findViewById(R.id.imageProfile);
   	imageProfileChange = findViewById(R.id.changePhoto);
   	fullName = findViewById(R.id.fullname);
   	userName = findViewById(R.id.username);
   	bio = findViewById(R.id.userBio);
   	progressBar = findViewById(R.id.progressBar);
 
   	firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
   	storageReference = FirebaseStorage.getInstance().getReference("uploads");
   	DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
   	reference.addValueEventListener(new ValueEventListener() {
       	@Override
       	public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
           	User user =  dataSnapshot.getValue(User.class);
               fullName.setText(user.getFullname());
               userName.setText(user.getUsername());
           	bio.setText(user.getBio());
         	  Glide.with(getApplicationContext()).load(user.getImageurl()).into(imageProfile);
       	}
 
       	@Override
       	public void onCancelled(@NonNull DatabaseError databaseError) {
 
       	}
   	});
 
   	close.setOnClickListener(new View.OnClickListener() {
       	@Override
       	public void onClick(View v) {
           	finish();
       	}
   	});
 
       imageProfileChange.setOnClickListener(new View.OnClickListener() {
       	@Override
       	public void onClick(View v) {
           	CropImage.activity().setAspectRatio(1,1).setCropShape(CropImageView.CropShape.OVAL).start(EditProfileActivity.this);
 
       	}
   	});
   	imageProfile.setOnClickListener(new View.OnClickListener() {
       	@Override
       	public void onClick(View v) {
           	CropImage.activity().setAspectRatio(1,1).setCropShape(CropImageView.CropShape.OVAL).start(EditProfileActivity.this);
 
       	}
   	});
 
   	save.setOnClickListener(new View.OnClickListener() {
       	@Override
       	public void onClick(View v) {
              updateProfile(fullName.getText().toString(),userName.getText().toString(),bio.getText().toString());
       	}
   	});
 
   }
 
 
   private void updateProfile(String toString, String toString1, String toString2) {
   	progressBar.setVisibility(View.VISIBLE);
  	DatabaseReference databaseReference =  FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
   	HashMap<String,Object> hashMap = new HashMap<>();
       hashMap.put("fullname",toString);
       hashMap.put("username",toString1);
   	hashMap.put("bio",toString2);
 
   	databaseReference.updateChildren(hashMap);
   	progressBar.setVisibility(View.GONE);
   }
   private String getFileExtensisons(Uri uri){
   	ContentResolver contentResolver =  getContentResolver();
   	MimeTypeMap mimeTypeMap =  MimeTypeMap.getSingleton();
   	return  mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
   }
 
   private void uploadImage(){
   	progressBar.setVisibility(View.VISIBLE);
   	if(imageUri != null){
       	final StorageReference  fileReferance =  storageReference.child(System.currentTimeMillis()+"."+getFileExtensisons(imageUri));
       	uploadTask = fileReferance.putFile(imageUri);
       	uploadTask.continueWithTask(new Continuation() {
           	@Override
           	public Object then(@NonNull Task task) throws Exception {
               	if(!task.isSuccessful()){
                   	throw task.getException();
               	}
 
               	return fileReferance.getDownloadUrl();
           	}
       	}).addOnCompleteListener(new OnCompleteListener<Uri>() {
           	@Override
           	public void onComplete(@NonNull Task<Uri> task) {
               	if(task.isSuccessful()){
                 	Uri downloadUri = task.getResult();
           	      String myUrl = downloadUri.toString();
 
                 	DatabaseReference reference =  FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
                     HashMap<String,Object> hashMap = new HashMap<>();
	                 hashMap.put("imageurl",String.valueOf(myUrl));
 
                     reference.updateChildren(hashMap);
                     progressBar.setVisibility(View.GONE);
               	}else{
                   	Toast.makeText(EditProfileActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
               	}
           	}
       	}).addOnFailureListener(new OnFailureListener() {
           	@Override
           	public void onFailure(@NonNull Exception e) {
               	Toast.makeText(EditProfileActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
 
           	}
       	});
   	}else{
       	Toast.makeText(EditProfileActivity.this,"Dont image",Toast.LENGTH_SHORT).show();
   	}
 
   }
 
   @Override
   protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
   	super.onActivityResult(requestCode, resultCode, data);
 
   	if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
       	CropImage.ActivityResult result = CropImage.getActivityResult(data);
       	imageUri =  result.getUri();
       	uploadImage();
   	}else{
       	Toast.makeText(EditProfileActivity.this,"Image Error",Toast.LENGTH_SHORT).show();
   	}
   }
}
 
 
 
 
public class HomeActivity extends AppCompatActivity {
 
   BottomNavigationView bottomNavigationView;
   Fragment fragment = null;
  
   @Override
   protected void onCreate(@Nullable Bundle savedInstanceState) {
   	super.onCreate(savedInstanceState);
   	//Xml tarafında oluşturulan  layoutun id ile baglanması sağlanmıştır.
   	setContentView(R.layout.home_activity);
   	bottomNavigationView = findViewById(R.id.bottomNavigation);
       bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
   	Bundle bundle = getIntent().getExtras();
   	if(bundle != null){
       	String publisher = bundle.getString("publisherid","");
       	PreferencesUtil.getInstance(HomeActivity.this).putKeyValue(PreferencesUtil.PROFILE_ID,publisher);
           getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,new UserFragment()).commit();
   	}else {
           getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new HomeFragment()).commit();
   	}
   }
 
   private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
       	new BottomNavigationView.OnNavigationItemSelectedListener() {
   	@Override
   	public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
       	switch (menuItem.getItemId()){
           	case  R.id.home:
               	fragment = new HomeFragment();
               	break;
           	case R.id.search:
               	fragment = new SearchFragment();
   	            break;
           	case  R.id.addphoto:
               	fragment = null;
               	Intent intent = new Intent(HomeActivity.this,AddPhoto.class);
               	startActivity(intent);
               	break;
           	case R.id.notification:
               	fragment = new NotificationFragment();
               	break;
           	case R.id.user:
               	PreferencesUtil.getInstance(HomeActivity.this).putKeyValue(PreferencesUtil.PROFILE_ID, FirebaseAuth.getInstance().getCurrentUser().getUid());
               	fragment = new UserFragment();
               	break;
           	default:
               	break;
 
       	}
       	if(fragment != null){
           	getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,fragment).commit();
       	}
 
       	return true;
   	}
   };
}
 
 
 
public class LoginActivity extends AppCompatActivity {
 
   private TextInputLayout emailInputLayout;
   private TextInputEditText emailInputEditText;
   private TextInputLayout passwordInputLayout;
   private TextInputEditText passwordInputEditText;
   private Button loginBtn;
   private TextView forgotYourPassword;
   private TextView txtSignUp;
   private ProgressBar progressBar;
   private FirebaseAuth  auth;
   private ConstraintLayout constraintLayout;
   FirebaseUser firebaseUser;
 
   @Override
   protected void onStart() {
   	super.onStart();
   	firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
   	if(firebaseUser !=  null){
       	startActivity(new Intent(LoginActivity.this,HomeActivity.class));
       	finish();
   	}
   }
 
   @Override
   protected void onCreate(@Nullable Bundle savedInstanceState) {
   	super.onCreate(savedInstanceState);
   	setContentView(R.layout.login_activity);
   	emailInputLayout = findViewById(R.id.mailinputlayout);
   	emailInputEditText = findViewById(R.id.mailedttxt);
   	passwordInputLayout = findViewById(R.id.passwordinputlayout);
   	passwordInputEditText = findViewById(R.id.passwordedtedt);
   	loginBtn = findViewById(R.id.loginbuttonmail);
   	forgotYourPassword = findViewById(R.id.forgotyourpassword);
   	txtSignUp = findViewById(R.id.txtsignup);
   	progressBar = findViewById(R.id.progressbar);
   	constraintLayout =  findViewById(R.id.constraintLyt);
   	auth = FirebaseAuth.getInstance();
       forgotYourPassword.setOnClickListener(this::forgotYourPasswordClick);
       txtSignUp.setOnClickListener(this::signUpClick);
       loginBtn.setOnClickListener(this::login);
   }
 
   private void forgotYourPasswordClick(View view) {
       	Intent intent =  new Intent(LoginActivity.this,PasswordChangeActivity.class);
       	startActivity(intent);
   }
 
   private void login(View view) {
   	String strEmail =  emailInputEditText.getText().toString().trim();
   	String strPassword = passwordInputEditText.getText().toString().trim();
   	if(TextUtils.isEmpty(strEmail) || TextUtils.isEmpty(strPassword)){
       	Toast.makeText(getApplicationContext(), R.string.fill,Toast.LENGTH_SHORT).show();
       	return;
   	}else if (strPassword.length() <= 6){
       	Toast.makeText(getApplicationContext(),getString(R.string.password_error),Toast.LENGTH_SHORT).show();
       	return;
   	}if(strEmail.indexOf("@") == -1  || strEmail.indexOf(".com") == -1){
       	Toast.makeText(getApplicationContext(),R.string.error_mail,Toast.LENGTH_SHORT).show();
       	return;
   	}else{
       	progressBar.setVisibility(View.VISIBLE);
           auth.signInWithEmailAndPassword(strEmail,strPassword).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
           	@Override
           	public void onComplete(@NonNull Task<AuthResult> task) {
               	if(task.isSuccessful()) {
                   	Snackbar.make(constraintLayout,"Başarılı şekilde giriş yapıdı!", Snackbar.LENGTH_SHORT).show();
                   	DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(auth.getCurrentUser().getUid());
 
                       databaseReference.addValueEventListener(new ValueEventListener() {
                       	@Override
  	                     public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                               progressBar.setVisibility(View.GONE);
                           	Intent intent =  new Intent(LoginActivity.this , HomeActivity.class);
                               intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                               startActivity(intent);
                           	finish();
                       	}
                 	      @Override
                       	public void onCancelled(@NonNull DatabaseError databaseError) {
                               progressBar.setVisibility(View.GONE);
 
                       	}
                   	});
 
               	}else{
                   	Snackbar.make(constraintLayout,"Giriş Hatası:" + task.getException().getMessage(), Snackbar.LENGTH_SHORT).show();
               	}
           	}
       	});
 
   	}
   }
 
   private void signUpClick(View view) {
   	Intent intent =  new Intent(LoginActivity.this,RegisterActivity.class);
   	startActivity(intent);
   }
}
 
 
public class OptionsActivity extends AppCompatActivity {
   TextView logout,settings;
   @Override
   protected void onCreate(Bundle savedInstanceState) {
   	super.onCreate(savedInstanceState);
   	setContentView(R.layout.activity_options);
   	logout = findViewById(R.id.logout);
   	settings = findViewById(R.id.settings);
   	Toolbar toolbar =findViewById(R.id.toolbar);
   	setSupportActionBar(toolbar);
   	getSupportActionBar().setTitle(R.string.options);
   	getSupportActionBar().setDisplayHomeAsUpEnabled(true);
   	toolbar.setNavigationOnClickListener(new View.OnClickListener() {
       	@Override
       	public void onClick(View v) {
           	finish();
       	}
   	});
 
   	logout.setOnClickListener(new View.OnClickListener() {
       	@Override
       	public void onClick(View v) {
  	         FirebaseAuth.getInstance().signOut();
           	PreferencesUtil.getInstance(getApplicationContext()).clearAll();
           	startActivity(new Intent(OptionsActivity.this,LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
//                finish();
       	}
   	});
 
   }
}
 
 
public class PasswordChangeActivity extends AppCompatActivity {
 
   FirebaseAuth auth;
   EditText mailEdt;
   Button changePasswordBtn;
   LinearLayout linearLayout ;
   @Override
   protected void onCreate(@Nullable Bundle savedInstanceState) {
   	super.onCreate(savedInstanceState);
   	setContentView(R.layout.password_change_activity);
   	mailEdt =  findViewById(R.id.passwordedtedt);
   	changePasswordBtn =  findViewById(R.id.passwordChangeBtn);
   	linearLayout = findViewById(R.id.changePasswordLyt);
   	auth = FirebaseAuth.getInstance();
   	changePasswordBtn.setOnClickListener(new View.OnClickListener() {
       	@Override
       	public void onClick(View v) {
         	  if(TextUtils.isEmpty(mailEdt.getText()) || mailEdt.getText().toString().indexOf("@") == -1){
               	Snackbar.make(linearLayout,"Mail adresinizi kontrol ediniz!", Snackbar.LENGTH_SHORT).show();
           	}else{
               	auth.sendPasswordResetEmail(mailEdt.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                   	@Override
                   	public void onComplete(@NonNull Task<Void> task) {
                       	if(task.isSuccessful()){
                           	Snackbar.make(linearLayout,"The link for new password sent into your mail address. ", Snackbar.LENGTH_SHORT).show();
                           	Handler handler =  new Handler();
                        	   handler.postDelayed(new Runnable() {
                               	@Override
                               	public void run() {
                              	
	                                   Intent intent = new Intent(PasswordChangeActivity.this,LoginActivity.class);
                                       startActivity(intent);
                               	}
                           	},3000);
 	                      }else{
                           	Snackbar.make(linearLayout,"Mail gönderme hatası! ", Snackbar.LENGTH_SHORT).show();
                       	}
                   	}
               	});
           	}
       	}
 	  });
 
   }
}
 
 
public class PostDetailFragment extends Fragment{
 
   String postid;
   private RecyclerView recyclerView;
   private PostAdapter postAdapter;
   private List<Post> postList;
   private FirebaseUser firebaseUser;
   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container,
                        	Bundle savedInstanceState) {
 
  	View view = inflater.inflate(R.layout.fragment_post_detail, container, false);
  	postid = PreferencesUtil.getInstance(getContext()).getString("postid","");
  	firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
  	recyclerView = view.findViewById(R.id.recyclerView);
  	recyclerView.setHasFixedSize(true);
   	LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
       recyclerView.setLayoutManager(linearLayoutManager);
   	postList = new ArrayList<>();
 
   	if(PreferencesUtil.getInstance(getContext()).getString(PreferencesUtil.PROFILE_ID,"").equals(firebaseUser.getUid()))
       	postAdapter = new PostAdapter(getContext(),postList,true);
   	else
       	postAdapter = new PostAdapter(getContext(),postList,false);
 
   	recyclerView.setAdapter(postAdapter);
   	readPost();
   	return view;
   }
 
   private void readPost(){
   	DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Posts").child(postid);
       databaseReference.addValueEventListener(new ValueEventListener() {
       	@Override
       	public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
           	postList.clear();
           	Post post =  dataSnapshot.getValue(Post.class);
           	postList.add(post);
               postAdapter.notifyDataSetChanged();
       	}
 
       	@Override
       	public void onCancelled(@NonNull DatabaseError databaseError) {
 
       	}
   	});
   }
}
 
 
 
public class RegisterActivity extends AppCompatActivity {
 
   EditText userName,fullName,email,password,phone,rePassword,old;
   TextView logintxt;
   Button registerBtn;
   ToggleButton toggleGender;
   ProgressBar progressBar;
   FirebaseAuth auth;
   DatabaseReference reference;
   String gender;
  
   @Override
   protected void onCreate(Bundle savedInstanceState) {
  	
  	 super.onCreate(savedInstanceState);
   	setContentView(R.layout.register_activity);
   	userName = findViewById(R.id.username);
   	fullName = findViewById(R.id.fullname);
   	email = findViewById(R.id.email);
   	password = findViewById(R.id.password);
	   logintxt = findViewById(R.id.logintxt);
   	registerBtn = findViewById(R.id.registerBtn);
   	progressBar = findViewById(R.id.progressBar);
   	toggleGender =  findViewById(R.id.toggleGender);
   	phone = findViewById(R.id.phoneNumber);
	   rePassword = findViewById(R.id.repassword);
   	old = findViewById(R.id.old);
   	auth =  FirebaseAuth.getInstance();
   	gender = "Male";
   	toggleGender.setOnClickListener(new View.OnClickListener() {
       	@Override
       	public void onClick(View v) {
           	if(toggleGender.isChecked()){
               	gender = "Female";
           	}else{
               	gender = "Male";
           	}
       	}
   	});
       logintxt.setOnClickListener(this::loginTxtClick);
       registerBtn.setOnClickListener(this::userSave);
   }
 
   private void userSave(View view) {
   	progressBar.setVisibility(View.VISIBLE);
   	String strUserName = userName.getText().toString();
   	String strFullName =  fullName.getText().toString();
   	String strEmail = email.getText().toString();
   	String strPassword =  password.getText().toString();
   	String strPhone = phone.getText().toString();
   	String olds = old.getText().toString();
   	if(TextUtils.isEmpty(strUserName) || TextUtils.isEmpty(strFullName) || TextUtils.isEmpty(strEmail) || TextUtils.isEmpty(strPassword)){
       	Toast.makeText(getApplicationContext(), R.string.fill,Toast.LENGTH_SHORT).show();
       	return;
   	}else if (password.length() <= 6){
       	Toast.makeText(getApplicationContext(),getString(R.string.password_error),Toast.LENGTH_SHORT).show();
       	return;
   	}else if(!password.getText().toString().equals(rePassword.getText().toString())){
       	Toast.makeText(getApplicationContext(),R.string.passwordequals,Toast.LENGTH_SHORT).show();
   	}else if(olds.isEmpty() || (Integer.valueOf(olds)<15 ||  Integer.valueOf(olds)>100) ) {
       	Toast.makeText(getApplicationContext(),R.string.pleaseold,Toast.LENGTH_SHORT).show();
       	return;
   	}
   	else{
           register(strUserName,strFullName,strEmail,strPassword, FasherUtil.isNullOrEmpty(strPhone) ? "" : strPhone,olds);
   	}
   }
 
   private void register(String strUserName, String strFullName, String strEmail, String strPassword,String strPhone,String old) {
       auth.createUserWithEmailAndPassword(strEmail,strPassword).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
       	
@Override
       	public void onComplete(@NonNull Task<AuthResult> task) {
           	if(task.isSuccessful()){
               	FirebaseUser firebaseUser = auth.getCurrentUser();
               	String userid = firebaseUser.getUid();
 
               	reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userid);
               	HashMap<String,Object> hashMap = new HashMap<>();
                   hashMap.put("id",userid);
               	hashMap.put("username",strUserName.toLowerCase());
                   hashMap.put("fullname",strFullName);
                   hashMap.put("bio","");
                   hashMap.put("gender",gender);
                   hashMap.put("phone",strPhone);
      	         hashMap.put("old",old);
               	String photoUrl;
               	if (gender.equals("Female")){
                   	photoUrl = "https://firebasestorage.googleapis.com/v0/b/fasher-12a8a.appspot.com/o/girl.png?alt=media&token=bfe5ccd0-c8fa-47a4-9926-981123b12e6f";
               	}else{
                   	photoUrl = "https://firebasestorage.googleapis.com/v0/b/fasher-12a8a.appspot.com/o/man-user.png?alt=media&token=9a144270-61c0-4c7a-a4fc-7bd890181ef2";
               	}
                   hashMap.put("imageurl",photoUrl);
                   reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                   	@Override
                   	public void onComplete(@NonNull Task<Void> task) {
                           if(task.isSuccessful()){
                               progressBar.setVisibility(View.GONE);
                           	Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                          	 intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                               startActivity(intent);
                       	}else{
                           	Toast.makeText(getApplicationContext(),"Bilgiler yüklenir hata oluştu!",Toast.LENGTH_SHORT).show();
                               progressBar.setVisibility(View.GONE);
                       	}
                   	}
               	});
 
           	}else {
               	progressBar.setVisibility(View.GONE);
               	Toast.makeText(getApplicationContext(), R.string.login_error,Toast.LENGTH_SHORT).show();
           	}
       	}
   	});
   }
 
   private void loginTxtClick(View view) {
   	Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
   	startActivity(intent);
   	finish();
 
   }
 
}
 
 
 
 
 
