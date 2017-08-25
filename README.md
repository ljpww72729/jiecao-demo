MediaPlayer，顾名思义是用于媒体文件播放的组件。Android中MediaPlayer通常与SurfaceView一起使用，当然也可以和其他控件诸如TextureView、SurfaceTexture等可以取得holder，用于MediaPlayer.setDisplay的控件一起使用。
对于现在的移动设备来说，媒体播放时一个非常重要的功能，所以掌握MediaPlayer对于Android程序员来说，也是一个基本要求了。由于媒体播放是一个比较复杂的事情，涉及到媒体资源的加载、解码等耗时耗资源的操作，所以MediaPlayer的使用相对其他组件变得复杂了许多。
掌握MediaPlayer需要先掌握MediaPlayer的工作过程和它的一些重要的方法，在Android Developer官网上可以搜到[MediaPlayer详细的讲解](!https://developer.android.com/reference/android/media/MediaPlayer.html)。
# MediaPlayer状态机
在官网上可以看到一张关于MediaPayer状态机的图，直观的阐述了MediaPlayer的工作过程，以及它的一些重要的方法的使用时机。如下：
![这里写图片描述](http://img.blog.csdn.net/20160926191458052)
从上图中，可以捋出MediaPlayer的一个最简单的使用流程：
1. 新建一个MediaPlayer: mPlayer=new MediaPlayer();
    * 通常在新建一个MediaPlayer实体后，会对给它增加需要的监听事件，MediaPlayer的监听事件有：
        1. MediaPlayer.OnPreparedListener：MediaPlayer进入准备完成的状态触发，表示媒体可以开始播放了。
        2. MediaPlayer.OnSeekCompleteListener：调用MediaPlayer的seekTo方法后，MediaPlayer会跳转到媒体指定的位置，当跳转完成时触发。需要注意的时，seekTo并不能精确的挑战，它的跳转点必须是媒体资源的关键帧。
        3. MediaPlayer.OnBufferingUpdateListener：网络上的媒体资源缓存进度更新的时候会触发。
        4. MediaPlayer.OnCompletionListener：媒体播放完毕时会触发。但是当OnErrorLister返回false，或者MediaPlayer没有设置OnErrorListener时，这个监听也会被触发。
        5. MediaPlayer.OnVideoSizeChangedListener：视频宽高发生改变的时候会触发。当所设置的媒体资源没有视频图像、MediaPlayer没有设置展示的holder或者视频大小还没有被测量出来时，获取宽高得到的都是0.
        6. MediaPlayer.OnErrorListener：MediaPlayer出错时会触发，无论是播放过程中出错，还是准备过程中出错，都会触发。
2. 将需要播放的资源路径交给MediaPlayer实体：mPlayer.setDataSource(source);
3. 让MediaPlayer去获取解析资源，调用prepare()或者prepareAsync()方法，前一个是同步方法，后一个是异步方法，通常我们用的比较多的是后者：mPlayer.prepareAsync();
4. 进入准备完成状态后，调用start()方法开始播放,如果是调用prepare()方法准备，在prepare()方法后，可以直接开始播放。如果是调用prepareAsync()方法准备，需要在OnPreparedListener()监听中开始播放：mPlayer.start();
这是一个最简单的播放流程，然而我们的需求绝不可能这么简单！通过以上流程我们会遇到很多问题。
# MediaPlayer使用常见问题
按照上面所说的流程来操作，我们会发现还有很多问题需要处理，比如说视频播放有声音没图像，切入后台后声音还在播放等等问题。综合一下，我们在安装上述流程走会有哪些问题以及我们解决一些问题后，还可能遇到哪些问题：
1. 视频播放有声音没图像。
2. 视频图像变形。
3. 切入后台后声音还在继续播放。
4. 切入后台再切回来，视频黑屏。
5. 暂停后切入后台，再切回来，并保持暂停状态会黑屏，seekTo也没有用。
6. 播放时会有一小段时间的黑屏。
7. 多个SurfaceView用来播放视频，滑动切换时会有上个视频的残影。
等等一些其他更多问题。最为典型的应该就是上述这些问题了。这些问题，仔细看看官网上对于MediaPlayer的讲解后，基本都不会是问题。恩，最后一个问题除外。相对MediaPlayer的状态机来说，MediaPlayer的各个方法的有效状态和无效状态为我们在使用MediaPlayer的具体方法时，提供了更好的指南。
# Valid and invalid states
感觉用有效状态和无效状态来翻译不太合适，干脆直接就用官方上面所说的Valid and invalid states吧。它指出了MediaPlayer中常用公有方法在那些状态下可以使用，在那些状态下不可以使用。
我们可以将所有的方法分为三类。
* 在任何状态下都可以使用的。比如设置监听，以及其他MediaPlayer中与资源无关的方法。需要特别注意的是setDisplay和setSurface两个方法。
* 在MediaPlayer状态机中除Error状态都可以使用的。比如获取视频宽高、获取当前位置等。
* 对状态有诸多限制，需要严格遵循状态机流程的方法。 比如start、pause、stop等等方法。
具体如图所示：
![这里写图片描述](http://img.blog.csdn.net/20160928182925887)

# 常见问题讨论
针对上面提到的问题，通过MediaPlayer的状态机和它的常用方法的可用状态来进行讨论，我们就能找到相应的原因，因为代码是不会欺骗的。
1. **有声音没有图像**
视频播放有声音没图像也许是在使用MediaPlayer最容易出现的问题，几乎所有使用MediaPlayer的新手都会遇到。视频播放的图像呈现需要一个载体，需要利用MediaPlayer.setDisplay设置一个展示视频画面的SurfaceHolder，最终视频的每一帧图像是要绘制在Surface上面的。通常，**设置给MediaPlayer的SurfaceHolder未被创建，视频播放就注定没有图像**。
*  比如你先调用了setDisplay，但是这个时候holder是没有被创建的。视频就没有图像了。
*  或者你在setDisplay的时候holder确保了holder是被创建了，但是当因为一些原因holder被销毁了，视频也就没有图像了。
* 再者，你没有给展示视频的view设置合适的大小，比如都设置wrap_content，或者都设置0，也会导致SurfaceHolder不能被创建，视频也就没有图像了。
2. **视频图像变形**
Surface展示视频图像的时候，是不会去主动保证和呈现出来的图像和原始图像的宽高比例是一致的，所以我们需要自己去设置展示视频的View的宽高，以保证视频图像展示出来的时候不会变形。我认为比较合适的做法就是利用FrameLayout嵌套一个SurfaceView或者其他拥有Surface的View来作为视频图像播放的载体View，然后再OnVideoSizeChangeListener的监听回调中，对载体View的大小做更改。
3. **切入后台后声音还在继续播放**
这个问题只需要在onPause中暂停播放即可
4. **切入后台再切回来，视频黑屏**
诸如此类的黑屏问题，多是因为surfaceholder被销毁了，再切回来时，需要重新给MediaPlayer设置holder。
5. **播放时会有一小段时间的黑屏**
视频准备完成后，调用play进行播放视频，承载视频播放的View会是黑屏状态，我们只需要在播放前，给对应的Surface绘制一张图即可。
6. **多个SurfaceView用来播放视频，滑动切换时会有上个视频的残影**
当视频切换出界面，设置surfaceView的visiable状态为Gone，界面切回来时再设置为visiable即可。

# 视频播放Demo
最后在放上一个视频播放的简易demo。播放效果如下：
![效果图](%E5%BD%95%E5%B1%8F%E4%B8%93%E5%AE%B6160929174635.gif)

[源码Git地址](!https://code.csdn.net/junzia/mediaplayerdemo.git)